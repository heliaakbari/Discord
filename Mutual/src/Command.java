
public class Command {
    /**
     * possible keywords : newMsg,newUser, newServer, newGroup, newRel, newReaction,
     * getNewMsgs, getRequests, getGroupMsg, getPrivateMsgs, getGroupMembers, getServerMembers
     * deleteMsg, deleteServer , deleteGroup, changeUsername
     */
    private String keyword;
    private String channel;
    private String group;
    private String user;
    private Object primary;
    private Object secondary;

}
