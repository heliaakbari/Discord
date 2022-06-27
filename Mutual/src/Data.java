
import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    private String keyword;
    private String user;
    private String server;
    private String channel;
    private Object primary;
    private Object secondary;

    private Data(String keyword){
        this.keyword=keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getUser() {
        return user;
    }

    public String getServer() {
        return server;
    }

    public String getChannel() {
        return channel;
    }

    public Object getPrimary() {
        return primary;
    }

    public Object getSecondary() {
        return secondary;
    }

    /**
     * returns if the sign up of a new member was successful or not
     */
    public static Data checkSignUp(String user, Boolean isSignedUp){
        Data data = new Data("checkSignUp");
        data.user = user;
        data.primary = isSignedUp;
        return data;
    }

    public static Data checkLogin(String user, Boolean isLoggedIn){
        Data data = new Data("checkLogin");
        data.user = user;
        data.primary = isLoggedIn;
        return data;
    }
    /**
     * returns if the new channel was successfully created
     */
    public static Data checkNewChannel(String creator, String server, String channel, Boolean isCreated){
        Data data = new Data("checkNewChannel");
        data.user = creator;
        data.server = server;
        data.channel = channel;
        data.primary = isCreated;
        return data;
    }
    public static Data checkDeleteChannel(String server,String channel,Boolean isDeleted){
        Data dt = new Data("checkDeleteChannel");
        dt.server = server;
        dt.channel = channel;
        dt.primary = isDeleted;
        return dt;
    }

    public static Data checkDeleteServer(String server,Boolean isDeleted){
        Data dt = new Data("checkDeleteServer");
        dt.server = server;
        dt.primary = isDeleted;
        return dt;
    }

    public static Data fake(){
        Data data = new Data("fake");
        return data;
    }

    public static Data checkNewRelation(String user,Object relation, Boolean created){
        Data data = new Data("checkNewRelation");
        data.user = user;
        data.primary = created;
        data.secondary = relation;
        return data;
    }

    public static Data newMsgs(String user, ArrayList<Message> messages){
        Data data = new Data("newMsgs");
        data.user = user;
        data.primary = messages;
        return data;
    }

    public static Data newChannelMsg(String server, String channel, Message msg){
        Data data = new Data("newChannelMsg");
        data.server = server;
        data.channel = channel;
        data.primary = msg;
        return data;
    }

    public static Data newPvMsg(String receiver, Message msg){
        Data data = new Data("newPvMsg");
        data.user = receiver;
        return data;
    }

    public static Data allFriendRequests(String user, ArrayList<String> allRequests){
        Data data = new Data("allFriendRequests");
        data.user = user;
        data.primary = allRequests;
        return data;
    }


    public static Data friends(String user, ArrayList<String> friends){
        Data data = new Data("friends");
        data.user = user;
        data.primary = friends;
        return data;
    }


    public static Data pinnedMsgs(String user,String server,String channel,ArrayList<Message> messages){
        Data data = new Data("pinnedMsgs");
        data.user = user;
        data.server = server;
        data.channel = channel;
        data.primary = messages;
        return data;
    }

    public static Data blockList(String user,ArrayList<String> peopleTheyBlocked){
        Data data = new Data("blockList");
        data.user = user;
        data.primary = peopleTheyBlocked;
        return data;
    }

    public static Data blockedBy(String user,ArrayList<String> peopleWhoBlockedThem){
        Data data = new Data("blockedBy");
        data.user = user;
        data.primary = peopleWhoBlockedThem;
        return data;
    }

    public static Data channelMsgs (String user, String server,String channel, ArrayList<Message> messages){
        Data data = new Data("channelMsgs");
        data.user = user;
        data.server= server;
        data.channel = channel;
        data.primary = messages;
        return data;
    }

    public static Data PvMsgs (String user, String theOtherPerson, ArrayList<Message> messages){
        Data data = new Data("PvMsgs");
        data.user = user;
        data.server= theOtherPerson;
        data.primary = messages;
        return data;
    }

    public static Data channelMembers(String user,String server,String channel, ArrayList<String> members){
        Data data = new Data("channelMembers");
        data.user = user;
        data.server = server;
        data.channel = channel;
        data.primary = members;
        return data;
    }

    public static Data serverMembers(String user,String server, HashMap<String,Role> membersAndRoles){
        Data data = new Data("serverMembers");
        data.user = user;
        data.server = server;
        data.primary = membersAndRoles;
        return data;
    }

    public static Data userChannels(String user,String server, ArrayList<String> channels){
        Data data = new Data("userChannels");
        data.user = user;
        data.server = server;
        data.primary = channels;
        return data;
    }

    public static Data userServers(String user, ArrayList<String> servers){
        Data data = new Data("userServers");
        data.user = user;
        data.primary = servers;
        return data;
    }

    public static Data userInfo(String username,User user){
        Data data = new Data ("userInfo");
        data.user= username;
        data.primary = user;
        return data;
    }

    public static Data reactions(String user,Message message,HashMap<String,Integer> reactions){
        Data data = new Data("reactions");
        data.primary = reactions;
        data.secondary = message;
        return data;
    }

    public static Data role(String user,String server,Role role){
        Data data = new Data("role");
        data.primary= role;
        data.user = user;
        data.server = server;
        return data;
    }

    public static Data checkChangeUsername(String oldName,String newName,Boolean successful){
        Data data = new Data("checkChangeUsername");
        data.user = oldName;
        data.secondary= newName;
        data.primary = successful;
        return data;
    }

    public static Data checkChangeServerName(String user,String oldName,String newName,Boolean successful){
        Data data = new Data("checkChangeServerName");
        data.user = user;
        data.server=oldName;
        data.secondary=newName;
        data.primary = successful;
        return data;
    }

    public static Data checkNewServer(String user,String server, Boolean successful){
        Data data = new Data("checkNewServer");
        data.user = user;
        data.server = server;
        data.primary = successful;
        return data;
    }

    public static Data directChats(String user,ArrayList<String> chats){
        Data data = new Data("directChats");
        data.primary = chats;
        data.user = user;
        return  data;
    }
}

