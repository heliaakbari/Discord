
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

    private Command(String keyword){
        this.keyword= keyword;
    }
    //for new message: to be decided

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
    public static Command newRequest(Object relationship){
        Command cmd = new Command("newRequest");
        cmd.primary= relationship;
        return cmd;
    }

    //for new reaction: newReaction, user=reaction sender, primary= message, secondary = type of reaction(enum)
    public static Command newReaction(Object reaction){
        Command cmd = new Command("newReaction");
        cmd.primary= reaction;
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


    //for getting list of someone's friends and their status: keyword=getFriends  user = username
    public static Command getFriends(String user){
        Command cmd = new Command("getFriends");
        cmd.user= user;
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
}
