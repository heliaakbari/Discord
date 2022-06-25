
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

    /**
     *returns if the sign up of a new member was successful or not
     */
    public static Data checkSignUp(String user, boolean isSignedUp){
        Data data = new Data("checkSignUp");
        data.user = user;
        data.primary = isSignedUp;
        return data;
    }

    /**
     * returns if the new channel was successfully created
     */
    public static Data checkNewChannel(String creator, String server, String channel, boolean isCreated){
        Data data = new Data("checkNewChannel");
        data.user = creator;
        data.server = server;
        data.channel = channel;
        data.primary = isCreated;
        return data;
    }

    public static Data checkNewRelation(String user,Object relation, boolean created){
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

    public static Data changeUsername(String user,boolean changed){
        Data data = new Data("checkChangedUsername");
        data.user = user;
        data.primary = changed;
        return data;
    }

    public static Data checkNewServer(String user,String server,boolean isCreated){
        Data data = new Data("checkNewServer");
        data.user = user;
        data.server= server;
        data.primary = isCreated;
        return data;
    }

    public static Data userInfo(String username,User user){
        Data data = new Data ("userInfo");
        data.user= username;
        data.primary = user;
        return data;
    }

    public static Data pinnedMessages(String user,String server,String channel,ArrayList<Message> messages){
        Data data = new Data("pinnedMessages");
        data.primary = messages;
        data.user= user;
        data.server=server;
        data.channel = channel;
        return data;
    }

    public static Data role(String user,String server,Role role){
        Data data = new Data("role");
        data.primary= role;
        data.user = user;
        data.server = server;
        return data;
    }
}

