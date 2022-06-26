import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.nio.file.Files.readAllBytes;

public class CmdManager {

    private Statement stmt = null;
    private String filespath = "C:\\Users\\Rpipc\\Desktop\\dbfile";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public CmdManager(Statement stmt,String filepath) {
        switch(){
            case "newPvMsg" :
                newPvMsg(cmd);
                break;
            case "changeProfilePhoto":
                changeProfilePhoto(cmd);
                break;

        }
    }

    public void process(Command cmd){
        
    }
    public void newPvMsg(Command cmd){
        String sender = cmd.getUser();
        String receiver=(String) cmd.getSecondary();
        Message message = (Message) cmd.getPrimary();
        String date = message.getDateTime().format(dateTimeFormatter);
        if(message instanceof TextMessage){
            message = (TextMessage)message;
            String body = ((TextMessage) message).getText();
            try {
                stmt.executeUpdate(String.format("insert into pv_messages(sender,receiver,date,body,isfile,seen) values ('%s','%s','%s','%s',false,false);", sender, receiver, date, body));
                FeedBack.say(sender+"'s text message sent to "+receiver);
            }
            catch (SQLException e){
                FeedBack.say("sender+\"'s text message was not sent to \"+receiver");
            }
        }
        if(message instanceof FileMessage){
            ////to be completed
        }


    }

    public void changeProfilePhoto(Command cmd){
        String user = cmd.getUser();
        byte[] image = (byte[])cmd.getPrimary();
        String format = (String) cmd.getSecondary();
        String address = filespath + "\\profilePhoto_" + user + "." + format;
        Path url = Paths.get(address);
        File file = new File(address);
        file.delete();
        try{
            Files.write( url,image);
        }
        catch (IOException e){
            FeedBack.say("could not save profile photo of "+user);
        }
        try {
            stmt.executeUpdate(String.format("UPDATE users SET picturelink ='%s' WHERE username='%s')",url,user));
        }catch (SQLException e){
            FeedBack.say("could not update picture url of "+user);
        }
    }

    public void NewChannelMsg(Command cmd){
        String sender = cmd.getUser();
        Message message = (Message) cmd.getPrimary();
        String server=message.getSourceInfo().get(1);
        String channel = message.getSourceInfo().get(2);
        String date = message.getDateTime().format(dateTimeFormatter);
        if(message instanceof TextMessage){
            message = (TextMessage)message;
            String body = ((TextMessage) message).getText();
            try {
                stmt.executeUpdate(String.format("insert into channel_messages(sender,server,channel,date,body,isfile) values ('%s','%s','%s','%s',false);", sender, server,channel, date, body));
                FeedBack.say(sender+"'s text message sent to "+server+"/"+channel);
            }
            catch (SQLException e){
                FeedBack.say(sender+"'s text message was not sent to "+server+"/"+channel);
            }
        }
        if(message instanceof FileMessage){
            ////to be completed
        }


    }

    public void newUser(Command cmd){
        User user = (User) cmd.getPrimary();
        byte[] image = (byte[])user.getProfilePhoto();
        String format = (String) cmd.getSecondary();
        String address = filespath + "\\profilePhoto_" + user.getUsername() + "." + format;
        Path url = Paths.get(address);
        try {
            ResultSet r = stmt.executeQuery(String.format("select count(*) as C1 from server_members where username='%s'", user.getUsername()));
            r.next();
            int count = r.getInt("C1");
            if (count > 0) {
                throw new SQLException();
            }
        }
        catch (SQLException e){
            FeedBack.say("a member with username: "+user.getUsername()+ "already exists");
        }
        try{
            Files.write( url,image);
            FeedBack.say("profile photo of "+user.getUsername()+" is saved");
        }
        catch (IOException e){
            FeedBack.say("could not save profile photo of "+user.getUsername());
        }
        try {
            stmt.executeUpdate(String.format("insert into users values ('%s','%s','%s','%S','%S','%S');", user.getUsername(), user.getPassword(), user.getPhoneNum(), user.getEmail(), user.getStatus() == null ? null : user.getStatus().toString(), address));
        }
        catch (SQLException e){
            FeedBack.say("could not add new User with username : "+user.getUsername());
        }
    }

    public void newChannel(Command cmd){
        try {
            ResultSet r = stmt.executeQuery(String.format("select count(*) as C1 from channel_members where channel='%s'",cmd.getChannel()));
            r.next();
            int count = r.getInt("C1");
            if(count>0){
                throw new SQLException();
            }
            stmt.executeUpdate(String.format("insert into channel_members values ('%s,'%s','%s',%s');", cmd.getUser(), cmd.getServer(), cmd.getChannel(), LocalDateTime.now().format(dateTimeFormatter)));
            FeedBack.say("channel "+cmd.getChannel()+" created successfully");

        }
        catch(SQLException e){
            FeedBack.say("could not create channel "+cmd.getChannel());
        }
    }

    public void newRelation(Command cmd){
        Relationship rel = (Relationship) cmd.getPrimary();
        String statement = new String();
        try{
            ResultSet r = stmt.executeQuery("SELECT count(*) as C1 from users where username='"+rel.getReceiver()+"'");
            r.next();
            if((int)r.getInt("C1")<=0){
                throw new SQLException();
            }
        }
        catch (SQLException s){
            FeedBack.say("such user doesnt exists");
            s.printStackTrace();
            return;
        }
        if (rel == Relationship.Block) {
            String s1 = String.format("delete from relationships where (receiver='%s' and sender='%s') or (sender='%s' and receiver='%s');",rel.getReceiver(),rel.getSender(),rel.getReceiver(),rel.getSender());
            String s2 = String.format("insert into relationships values('%s','%s','%s');",rel.getSender(),rel.getReceiver(),rel.toString());
            statement = s1 +"\n"+s2;
        }
        else if(rel==Relationship.Friend){
            String s1 = String.format("delete from relationships where (receiver='%s' and sender='%s') or (sender='%s' and receiver='%s');",rel.getReceiver(),rel.getSender(),rel.getReceiver(),rel.getSender());
            String s2 = String.format("insert into relationships values('%s','%s','%s');",rel.getSender(),rel.getReceiver(),rel.toString());
            statement = s1 +"\n"+s2;
        }
        else if(rel==Relationship.Friend_pending){
            try {
                ResultSet res = stmt.executeQuery(String.format("select count(*) as C1 from relationships where sender='%s' and receiver='%s' and status='Block';", rel.getReceiver(), rel.getSender()));
                res.next();
                int count = res.getInt("C1");
                if (count > 0) {
                    throw new SQLException();
                }
                String s1 = String.format("delete from relationships where receiver='%s' and sender='%s';",rel.getReceiver(),rel.getSender());
                String s2 = String.format("insert into relationships values('%s','%s','%s');",rel.getSender(),rel.getReceiver(),rel.toString());
                statement = s1 + "\n" +s2;
            }
            catch (SQLException e){
                statement = null;
                e.printStackTrace();
                FeedBack.say("cannot send request bc they have blocked you");
            }
        }

        try{
            if(statement!=null){
                stmt.executeUpdate(statement);
            }
        }catch (SQLException e){
            e.printStackTrace();
            FeedBack.say("could not add this relationship");
        }

    }

    public void newReaction(Command cmd){
        Message message = (Message) cmd.getPrimary();
        String date = message.getDateTime().format(dateTimeFormatter);
        try{
            stmt.executeUpdate(String.format("delete from reactions where reactionSender='%s' and messageSender='%s' and messageDate='%s'",cmd.getUser(),message.getSourceInfo().get(0),date));
            stmt.executeUpdate(String.format("insert into reactions values ('%s','%s','%s','%s','%s','%s');",cmd.getUser(),message.getSourceInfo().get(1),message.getSourceInfo().get(2),date,message.getSourceInfo().get(0),(String)cmd.getServer()));
        }catch (SQLException s){
            s.printStackTrace();
        }
    }

    //getNewMsgs to be written
    public void getNewMsgs(Command cmd){

    }
    public Data getRequests(Command cmd){
        ResultSet rs = null;
        ArrayList<String> requesters = new ArrayList<>();
        try{
            rs = stmt.executeQuery(String.format("select sender as S from relationships where status='%s' and receiver='%s'",Relationship.Friend_pending.toString(),cmd.getUser()));

            while (rs.next()){
                requesters.add(rs.getString("S"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.allFriendRequests(cmd.getUser(),requesters);
    }

    public Data friends(Command cmd){
        ResultSet rs = null;
        ArrayList<String> friends = new ArrayList<>();
        try{
            rs = stmt.executeQuery(String.format("select sender as S from relationships where status='%s' and receiver='%s'",Relationship.Friend.toString(),cmd.getUser()));

            while (rs.next()){
                friends.add(rs.getString("S"));
            }

            rs = stmt.executeQuery(String.format("select receiver as S from relationships where status='%s' and sender='%s'",Relationship.Friend.toString(),cmd.getUser()));

            while (rs.next()){
                friends.add(rs.getString("S"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.friends(cmd.getUser(),friends);
    }

    public Data getBlockList(Command cmd){
        ResultSet rs = null;
        ArrayList<String> blockeds = new ArrayList<>();
        try{
            rs = stmt.executeQuery(String.format("select receiver as S from relationships where status='%s' and sender='%s'",Relationship.Block.toString(),cmd.getUser()));

            while (rs.next()){
                blockeds.add(rs.getString("S"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.blockList(cmd.getUser(),blockeds);
    }

    public Data getBlockedBy(Command cmd){
        ResultSet rs = null;
        ArrayList<String> blockedBys = new ArrayList<>();
        try{
            rs = stmt.executeQuery(String.format("select sender as S from relationships where status='%s' and receiver='%s'",Relationship.Block.toString(),cmd.getUser()));

            while (rs.next()){
                blockedBys.add(rs.getString("S"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.blockList(cmd.getUser(),blockedBys);
    }

    //get channel msgs

    //get pv messages

    public Data getChannelMembers(Command cmd){
        ResultSet rs = null;
        ArrayList<String> members = new ArrayList<>();
        try{
            rs = stmt.executeQuery(String.format("select username as S from channel_members where server='%s' and channel='%s'",cmd.getServer(),cmd.getChannel()));

            while (rs.next()){
                members.add(rs.getString("S"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.channelMembers(cmd.getUser(),cmd.getServer(),cmd.getChannel(),members);
    }

    public Data serverMembers(Command cmd){
        ResultSet rs = null;
        HashMap<String,Role> members = new HashMap<>();

        try{
            rs = stmt.executeQuery(String.format("select username as U,rolename as R,abilities as A from channel_members where server='%s'",cmd.getServer()));

            while (rs.next()){
                members.put(rs.getString("U"),new Role(rs.getString("A"),rs.getString("R")));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.serverMembers(cmd.getUser(),cmd.getServer(),members);
    }


    public void deleteServer(Command cmd){
        try{
            stmt.executeUpdate(String.format("Delete from server_members where server='%s'",cmd.getServer()));
            stmt.executeUpdate(String.format("Delete from channel_messages where server='%s'",cmd.getServer()));
            stmt.executeUpdate(String.format("Delete from channel_members where server='%s'",cmd.getServer()));
            stmt.executeUpdate(String.format("Delete from reactions where server='%s'",cmd.getServer()));
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    public void deleteChannel(Command cmd){
        try{
            stmt.executeUpdate(String.format("Delete from channel_messages where server='%s' and channel='%s'",cmd.getServer(),cmd.getChannel()));
            stmt.executeUpdate(String.format("Delete from channel_members where server='%s' and channel='%s'",cmd.getServer(),cmd.getChannel()));
            stmt.executeUpdate(String.format("Delete from reactions where server='%s' and channel='%s'",cmd.getServer(),cmd.getChannel()));
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }

    //change username
    public void changeUsername(Command cmd){
        String old = cmd.getUser();
        String newName = (String) cmd.getPrimary();

        try {
            ResultSet r = stmt.executeQuery(String.format("select count(*) as C1 from users where username='%s'", cmd.getUser()));
            r.next();
            int count = r.getInt("C1");
            if (count > 0) {
                throw new SQLException();
            }
            stmt.executeUpdate(String.format("UPDATE relationships SET sender = '%s' WHERE sender='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE relationships SET receiver = '%s' WHERE receiver='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE channel_members SET username = '%s' WHERE username='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE channel_messages SET sender = '%s' WHERE sender='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE pv_messages SET receiver = '%s' WHERE receiver='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE pv_messages SET sender = '%s' WHERE sender='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE reactions SET reactionsender = '%s' WHERE reactionsender='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE reactions SET messagesender = '%s' WHERE messagesender='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE server_members SET username = '%s' WHERE username='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE users SET username = '%s' WHERE username='%s';",newName,old));
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }
    //change server name
    public void changeServerName(Command cmd){
        String old = cmd.getServer();
        String newName = (String) cmd.getPrimary();

        try {
            ResultSet r = stmt.executeQuery(String.format("select count(*) as C1 from server_members where server='%s'",old));
            r.next();
            int count = r.getInt("C1");
            if (count > 0) {
                throw new SQLException();
            }

            stmt.executeUpdate(String.format("UPDATE channel_members SET server = '%s' WHERE server='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE channel_messages SET server = '%s' WHERE server='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE reactions SET server = '%s' WHERE server='%s';",newName,old));
            stmt.executeUpdate(String.format("UPDATE server_members SET server = '%s' WHERE server='%s';",newName,old));
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }
    //new server
    public void newServer(Command cmd){

        try {
            ResultSet r = stmt.executeQuery(String.format("select count(*) as C1 from server_members where server='%s'",cmd.getServer()));
            r.next();
            int count = r.getInt("C1");
            if (count > 0) {
                throw new SQLException();
            }
            stmt.executeUpdate(String.format("insert into server_members values('%s','%s','creator','111111111')",cmd.getUser(),cmd.getServer()));
        }
        catch (SQLException s){
            s.printStackTrace();
        }
    }
    //getuser
    public Data getUser(Command cmd){
        String username = cmd.getUser();
        User user = null;
        try{
            ResultSet rs = stmt.executeQuery(String.format("select * from users where username='%s'",username));
            rs.next();
            user = new User(username,rs.getString("PASSWORD"),rs.getString("EMAIL"));
            user.setPhoneNum(rs.getString("PHONE"));
            user.setStatus(rs.getString("STATUS"));
            user.setProfilePhoto(fileToBytes(rs.getString("PICTURELINK")),rs.getString("PICTUREFORMAT"));


        }catch (SQLException s){

        }
        return Data.userInfo(username,user);
    }
    //pinmsg
    public void pinMsg(Command cmd){
        Message message = (Message)cmd.getPrimary();
        try {
            if (message.getSourceInfo().size() == 3) {
                stmt.executeUpdate(String.format("update channel_messages set ispinned=true where date='%s' and sender='%s'",message.getDateTime().format(dateTimeFormatter),message.getSourceInfo().get(0)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    //ban from channel
    public void banFromChannel(Command cmd){
        try{
            stmt.executeUpdate(String.format("delete from channel_members where channel='%s' and server='%s' and username='%s'",cmd.getChannel(),cmd.getServer(),cmd.getUser()));
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //ban from server
    public void banFromServer(Command cmd){
        try{
            stmt.executeUpdate(String.format("delete from server_members where server='%s' and username='%s'",cmd.getServer(),cmd.getUser()));
            stmt.executeUpdate(String.format("delete from channel_members where server='%s' and username='%s'",cmd.getServer(),cmd.getUser()));
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //getrole
    public Data getRole(Command cmd){
        Data dt = Data.role(cmd.getUser(),(String) cmd.getPrimary(),cmd.getServer(),null);
        try{
            ResultSet rs = stmt.executeQuery(String.format("select rolename,abilities from channel_members where username='%s' and server='%s'",(String)cmd.getPrimary(),cmd.getServer()));
            while (rs.next()) {
                dt = Data.role(cmd.getUser(), (String) cmd.getPrimary(), cmd.getServer(), new Role(rs.getString("ROLENAME"), rs.getString("ABILITIES")));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return dt;
    }

    //lastseenall
    public void lastseenAll(Command cmd){
        try{
            stmt.executeUpdate(String.format("update channel_members set lastseen='%s' where username='%s'",LocalDateTime.now().format(dateTimeFormatter),cmd.getUser()));
            stmt.executeUpdate(String.format("update pv_messages set seen=true where receiver='%s' and date<'%s';",cmd.getUser(),LocalDateTime.now().format(dateTimeFormatter)));
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    //lastseenpv
    public void lastseenPv(Command cmd){
        try{
            stmt.executeUpdate(String.format("update pv_messages set seen=true where receiver='%s' and sender='%s' and date<'%s';",cmd.getUser(),(String)cmd.getPrimary(),LocalDateTime.now().format(dateTimeFormatter)));
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    //lastseenchannel
    public void lastseenChannel(Command cmd){
        try{
            stmt.executeUpdate(String.format("update channel_members set lastseen='%s' where username='%s' and channel='%s'",LocalDateTime.now().format(dateTimeFormatter),cmd.getUser(),cmd.getChannel()));
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    //get servers
    public Data userServers(Command cmd){
        ResultSet rs = null;
        ArrayList<String> servers = new ArrayList<>();

        try{
            rs = stmt.executeQuery(String.format("select server as U from server_members where username='%s'",cmd.getUser()));

            while (rs.next()){
                servers.add(rs.getString("U"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.userServers(cmd.getUser(),servers);
    }
    //get channels
    public Data userChannels(Command cmd){
        ResultSet rs = null;
        ArrayList<String> channels = new ArrayList<>();

        try{
            rs = stmt.executeQuery(String.format("select channel as U from channel_members where username='%s' and server='%s'",cmd.getUser(),cmd.getServer()));

            while (rs.next()){
                channels.add(rs.getString("U"));
            }
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        return Data.userChannels(cmd.getUser(),cmd.getServer(),channels);
    }


    public byte[] fileToBytes(String path){
        // file to byte[], Path
        byte[] bytes = null;
        try {
            bytes = readAllBytes(Paths.get(path));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }
}

