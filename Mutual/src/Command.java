
public class Command {
    /**
     * possible keywords : newMsg,newUser, newServer, newChannel, newRequest, newReaction,
     * getNewMsgs, getRequests, getChannelMsg, getPvMsgs, getChannelMembers, getServerMembers
     * getFriends, deleteServer , deleteChannel, changeUsername,changeServerName
     */
    private String keyword;
    private String server;
    private String channel;
    private String user;
    private Object primary;
    private Object secondary;

    public String getKeyword() {
        return keyword;
    }

    public String getChannel() {
        return channel;
    }

    public String getUser() {
        return user;
    }

    public String getServer() {
        return server;
    }

    public Object getPrimary() {
        return primary;
    }

    public Object getSecondary() {
        return secondary;
    }

    private Command(String keyword){
        this.keyword= keyword;
    }

    public static Command newPvMsg(String sender,String reciever,Message message){
        Command cmd = new Command("newPvMsg");
        cmd.user = sender;
        cmd.primary = message;
        cmd.secondary = reciever;
        return cmd;
    }

    public static Command changeProfilePhoto(String user, byte[] image , String format){
        Command cmd = new Command("changeProfilePhoto");
        cmd.user= user;
        cmd.primary=image;
        cmd.secondary=format;
        return cmd;
    }


    public static Command newChannelMsg(String sender,Message message){
        Command cmd = new Command("newChannelMsg");
        cmd.user = sender;
        cmd.primary= message;
        return cmd;
    }

    //for new User: keyword: newUser    primary: user object
    public static Command newUser(User user) {
        Command cmd = new Command("newUser");
        cmd.primary=user;
        return cmd;
    }
    //for new channel : keyword: newChannel server= server's name  channel= new channel's name   user= creator's name
    public static Command newChannel(String creator, String server, String channel){
        Command cmd = new Command("newChannel");
        cmd.user= creator;
        cmd.server=server;
        cmd.channel=channel;
        return cmd;
    }

    //for new request: keyword: newRequest, user= sender's name, primary = a request object
    public static Command newRelation(Relationship relation){
        Command cmd = new Command("newRelation");
        cmd.primary= relation;
        return cmd;
    }

    //for new reaction: newReaction, user=reaction sender, primary= message, secondary = type of reaction(enum)
    public static Command newReaction(String user,Message message, String type){
        Command cmd = new Command("newReaction");
        cmd.user = user;
        cmd.primary = message;
        cmd.secondary = type;
        return cmd;
    }

    //for getting all new messages: keyword=getNewMsgs, user=username
    public static Command getNewMsgs(String user){
        Command cmd = new Command("getNewMsgs");
        cmd.user=user;
        return cmd;
    }

    //for get requests : keyword=getRequests , user= username
    public static Command getRequests(String user){
        Command cmd = new Command("getRequests");
        cmd.user=user;
        return cmd;
    }

    //for getting list of someone's friends and their status: keyword=getFriends  user = username
    public static Command getFriends(String user){
        Command cmd = new Command("getFriends");
        cmd.user= user;
        return cmd;
    }

    public static Command getBlockList(String user){
        Command cmd = new Command("getBlockList");
        cmd.user= user;
        return cmd;
    }

    public static Command getBlockedBy(String user){
        Command cmd = new Command("getBlockedBy");
        cmd.user= user;
        return cmd;
    }

    //for getting channel's messages : keyword=getChannelMsg , user=username,
    //  channel = channel, server = server, primary = number of latest massages in INTEGER
    public static Command getChannelMsg(String user, String server, String channel,Integer numberOfMessages){
        Command cmd = new Command("getChannelMsg");
        cmd.user=user;
        cmd.channel= channel;
        cmd.server = server;
        cmd.primary= numberOfMessages;
        return cmd;
    }

    //for getting private massages: keyword=getPvMsgs , user = username,
    //primary = the other person's username, secondary= number of last messages
    public static Command getPvMsgs(String user,String theFriend, Integer numberOfMessages){
        Command cmd = new Command("getPvMsgs");
        cmd.user = user;
        cmd.primary = theFriend;
        cmd.secondary= numberOfMessages;
        return cmd;
    }
    //for getting list of channel members:keyword=getChannelMembers user = username, server = server,
    //channel=channel
    public static Command getChannelMembers(String user,String server, String channel){
        Command cmd = new Command("getChannelMembers");
        cmd.user=user;
        cmd.server= server;
        cmd.channel= channel;
        return cmd;
    }

    //for getting list of server members:keyword=getServerMembers user = username, server = server
    public static Command getServerMembers(String user,String server){
        Command cmd = new Command("getServerMembers");
        cmd.user= user;
        cmd.server= server;
        return cmd;
    }




    //for deleting server: keyword=deleteServer, user= user, server=server
    public static Command deleteServer(String user,String server){
        Command cmd = new Command("deleteServer");
        cmd.user = user;
        cmd.server = server;
        return cmd;
    }

    //for deleting channel: keyword=deleteChannel, user= user, server=server
    //channel=channel
    public static Command deleteChannel(String user,String server,String channel){
        Command cmd = new Command("deleteChannel");
        cmd.user=user;
        cmd.channel=channel;
        cmd.server=server;
        return cmd;
    }

    //for changing username :keyword = changeUsername , user= old username,
    //primary = new username
    public static Command changeUsername(String oldName,String newName){
        Command cmd = new Command("changeUsername");
        cmd.user= oldName;
        cmd.primary=newName;
        return cmd;
    }

    //for changing server's name: keyword= changeServerName, server= old name.
    //user= user, primary= new servername
    public static Command changeServerName(String user, String oldName,String newName){
        Command cmd = new Command("changeServername");
        cmd.user= user;
        cmd.server=oldName;
        cmd.primary= newName;
        return cmd;
    }


    //for new server : keyword=newServer   server=server name, user = creator's username
    public static Command newServer(String creator,String serverName){
        Command cmd = new Command("newServer");
        cmd.user = creator;
        cmd.server= serverName;
        return cmd;
    }

    public static Command banFromChannel(String personToBeBanned,String server,String channel){
        Command cmd = new Command("banFromChannel");
        cmd.user=personToBeBanned;
        cmd.server=server;
        cmd.channel= channel;
        return cmd;
    }

    public static Command banFromServer(String personToBeBanned,String server){
        Command cmd = new Command("banFromServer");
        cmd.user = personToBeBanned;
        cmd.server = server;
        return cmd;
    }
    public static Command getServers(String user){
        Command cmd = new Command("getServers");
        cmd.user = user;
        return cmd;
    }

    public static Command getChannels(String user, String server){
        Command cmd = new Command("getChannels");
        cmd.user = user;
        cmd.server = server;
        return cmd;
    }

    public static Command getUser(String user){
        Command cmd = new Command("getUser");
        cmd.user= user;
        return cmd;
    }

    public static Command pinMsg(String user,Message message){
        Command cmd = new Command("pinMsg");
        cmd.user = user;
        cmd.primary= message;
        return cmd;
    }

    public static Command getRole(String you,String personYouaskAbout, String server){
        Command cmd = new Command("getRole");
        cmd.user = you;
        cmd.primary = personYouaskAbout;
        cmd.server= server;
        return cmd;
    }


    public static Command lastseenAll(String user){
        Command cmd = new Command("lastSeenAll");
        cmd.user= user;
        return cmd;
    }

    public static Command lastseenPv(String user,String theOtherPerson){
        Command cmd = new Command("lastseenPv");
        cmd.user=user;
        cmd.secondary = theOtherPerson;
        return cmd;
    }

    public static Command lastseenChannel(String user,String server,String channel){
        Command cmd = new Command("lastseenChannel");
        cmd.user=user;
        cmd.server = server;
        cmd.channel = channel;
        return cmd;
    }
}
