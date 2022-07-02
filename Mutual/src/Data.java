
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Data  implements Serializable {

    private static final long serialVersionUID = 757982467897459L;
    private String keyword;
    private String user;
    private String server;
    private String channel;
    private Serializable primary;
    private Serializable secondary;

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
     * returns if the signup of a new member was successful or not
     */
    public static @NotNull Data checkSignUp(String user, Boolean isSignedUp){
        Data data = new Data("checkSignUp");
        data.user = user;
        data.primary = isSignedUp;
        return data;
    }

    public static @NotNull Data checkLogin(String user, Boolean isLoggedIn){
        Data data = new Data("checkLogin");
        data.user = user;
        data.primary = isLoggedIn;
        return data;
    }
    /**
     * returns if the new channel was successfully created
     */
    public static @NotNull Data exitChat(String user){
        Data data = new Data("exitChat");
        data.user = user;
        return data;
    }
    public static @NotNull Data checkNewChannel(String creator, String server, String channel, Boolean isCreated){
        Data data = new Data("checkNewChannel");
        data.user = creator;
        data.server = server;
        data.channel = channel;
        data.primary = isCreated;
        return data;
    }
    public static @NotNull Data checkDeleteChannel(String server, String channel, Boolean isDeleted){
        Data dt = new Data("checkDeleteChannel");
        dt.server = server;
        dt.channel = channel;
        dt.primary = isDeleted;
        return dt;
    }

    public static @NotNull Data checkDeleteServer(String server, Boolean isDeleted){
        Data dt = new Data("checkDeleteServer");
        dt.server = server;
        dt.primary = isDeleted;
        return dt;
    }

    @Contract(pure = true)
    public static @NotNull Data fake(){
        Data data = new Data("fake");
        return data;
    }


    public static @NotNull Data newMsgs(String user, ArrayList<Message> messages){
        Data data = new Data("newMsgs");
        data.user = user;
        data.primary = messages;
        return data;
    }

    public static @NotNull Data newChannelMsg(String sender,String server, String channel, Message msg){
        Data data = new Data("newChannelMsg");
        data.user = sender;
        data.server = server;
        data.channel = channel;
        data.primary = msg;
        return data;
    }

    public static @NotNull Data newPvMsg(String receiver, Message msg){
        Data data = new Data("newPvMsg");
        data.user = receiver;
        data.primary = msg;
        return data;
    }

    public static Data giveFilePath(String filepath){
        Data data = new Data("giveFilePath");
        data.primary = filepath;
        return data;
    }
    public static @NotNull Data allFriendRequests(String user, ArrayList<String> allRequests){
        Data data = new Data("allFriendRequests");
        data.user = user;
        data.primary = allRequests;
        return data;
    }


    public static @NotNull Data friends(String user, ArrayList<String> friends){
        Data data = new Data("friends");
        data.user = user;
        data.primary = friends;
        return data;
    }


    public static @NotNull Data pinnedMsgs(String user, String server, String channel, ArrayList<Message> messages){
        Data data = new Data("pinnedMsgs");
        data.user = user;
        data.server = server;
        data.channel = channel;
        data.primary = messages;
        return data;
    }

    public static @NotNull Data blockList(String user, ArrayList<String> peopleTheyBlocked){
        Data data = new Data("blockList");
        data.user = user;
        data.primary = peopleTheyBlocked;
        return data;
    }

    public static @NotNull Data blockedBy(String user, ArrayList<String> peopleWhoBlockedThem){
        Data data = new Data("blockedBy");
        data.user = user;
        data.primary = peopleWhoBlockedThem;
        return data;
    }

    public static @NotNull Data channelMsgs (String user, String server, String channel, ArrayList<Message> messages){
        Data data = new Data("channelMsgs");
        data.user = user;
        data.server= server;
        data.channel = channel;
        data.primary = messages;
        return data;
    }

    public static @NotNull Data PvMsgs (String user, String theOtherPerson, ArrayList<Message> messages){
        Data data = new Data("PvMsgs");
        data.user = user;
        data.server= theOtherPerson;
        data.primary = messages;
        return data;
    }

    public static @NotNull Data channelMembers(String user, String server, String channel, ArrayList<String> members){
        Data data = new Data("channelMembers");
        data.user = user;
        data.server = server;
        data.channel = channel;
        data.primary = members;
        return data;
    }

    public static @NotNull Data serverMembers(String user, String server, HashMap<String,Role> membersAndRoles){
        Data data = new Data("serverMembers");
        data.user = user;
        data.server = server;
        data.primary = membersAndRoles;
        return data;
    }

    public static @NotNull Data userChannels(String user, String server, ArrayList<String> channels){
        Data data = new Data("userChannels");
        data.user = user;
        data.server = server;
        data.primary = channels;
        return data;
    }

    public static @NotNull Data userServers(String user, ArrayList<String> servers){
        Data data = new Data("userServers");
        data.user = user;
        data.primary = servers;
        return data;
    }

    public static @NotNull Data userInfo(String username, User user){
        Data data = new Data ("userInfo");
        data.user= username;
        data.primary = user;
        return data;
    }

    public static @NotNull Data reactions(String user, Message message, HashMap<String,Integer> reactions){
        Data data = new Data("reactions");
        data.primary = reactions;
        data.secondary = message;
        return data;
    }

    public static @NotNull Data role(String user, String server, Role role){
        Data data = new Data("role");
        data.primary= role;
        data.user = user;
        data.server = server;
        return data;
    }

    public static @NotNull Data checkChangeUsername(String oldName, String newName, Boolean successful){
        Data data = new Data("checkChangeUsername");
        data.user = oldName;
        data.secondary= newName;
        data.primary = successful;
        return data;
    }

    public static @NotNull Data checkChangeServerName(String user, String oldName, String newName, Boolean successful){
        Data data = new Data("checkChangeServerName");
        data.user = user;
        data.server=oldName;
        data.secondary=newName;
        data.primary = successful;
        return data;
    }

    public static @NotNull Data checkNewServer(String user, String server, Boolean successful){
        Data data = new Data("checkNewServer");
        data.user = user;
        data.server = server;
        data.primary = successful;
        return data;
    }

    public static @NotNull Data directChats(String user, ArrayList<String> chats){
        Data data = new Data("directChats");
        data.primary = chats;
        data.user = user;
        return  data;
    }
}

