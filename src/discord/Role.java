package discord;

import java.util.ArrayList;
import java.util.Arrays;

public class Role {
//    private int creatChannel;
//    private int removeChannel;
//    private int removeMember;
//    private int restrictMember;
//    private int banMember;
//    private int changeServerName;
//    private int seeChatHistory;
//    private int pinMessage;
//    private int deleteServer;
    private String values;
    private final ArrayList<String> abilities = new ArrayList<>(Arrays.asList("creat channel", "remove channel", "remove member",
            "restrict member", "ban member", "change server name", "see chat history", "pin message", "delete server"));

//    public Role(int creatChannel, int removeChannel, int removeMember, int restrictMember, int banMember,
//                int changeServerName, int seeChatHistory, int pinMessage, int deleteServer) {
//        this.creatChannel = creatChannel;
//        this.removeChannel = removeChannel;
//        this.removeMember = removeMember;
//        this.restrictMember = restrictMember;
//        this.banMember = banMember;
//        this.changeServerName = changeServerName;
//        this.seeChatHistory = seeChatHistory;
//        this.pinMessage = pinMessage;
//        this.deleteServer = deleteServer;
//    }


    public Role(String values) {
        this.values = values;
    }

    @Override
    public String toString() {
        int choiceNumber = 1;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++){
            if (values.charAt(i) == 1){
                stringBuilder.append(choiceNumber + " : " + abilities.get(i) + "\n");
                choiceNumber++;
            }
        }

        return stringBuilder.toString();
    }
}
