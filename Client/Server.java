import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    private String currentServerName;
    private InputHandler inputHandler;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Command cmd;
    private Data data;

    public Server(String name, ObjectOutputStream out, ObjectInputStream in){
        currentServerName = name;
        inputHandler = new Console();
        this.out = out;
        this.in = in;
    }

    public void enterServer(String currentUsername){
        // دوتا اپشن لیست قابلیت ها و لیست کانال ها برای کاربر تو هر سرور نمایش داده میشه
        int choice;
        do {
             choice = inputHandler.showMenu("1) actions list\n2) channels list\npress 0 to exit ", 2);
             if (choice == 1)
                 actionsList(currentUsername);
             else if (choice == 2)
                 channelsList(currentUsername);

        } while (choice != 0);

    }

    public void channelsList(String currentUsername){

        // create a command to get a list of channels in this server
        cmd = Command.userChannels(currentUsername, currentServerName);
        transfer();
        ArrayList<String> channelsList = (ArrayList<String>) data.getPrimary();
        channelsList.add("press 0 to exit");
        int choice;
        String channelName;

        do {
            choice = inputHandler.showMenu(channelsList);
            channelName = channelsList.get(choice - 1);

            int action;
            do {
                action = inputHandler.showMenu("1) start chatting\n2) see the channel members\npress 0 to exit", 2);

                if (action == 1) {
                    inputHandler.printMsg("you can type your messages now, to react to a message type the message number and then your reaction.\n" +
                            "to send a file type 'send file'. to download a file type 'open file <file name>'. press 0 to exit the chat");
                    // transfer messages with server
                    MessageReader messageReader = new MessageReader(in, inputHandler);
                    messageReader.start();

                    MessageWriter messageWriter = new MessageWriter(out, new ArrayList<>(Arrays.asList(currentUsername, channelName, currentServerName)));
                    messageWriter.start();
                }
                else if (action == 2) {
                    // creat a command to get the members
                    cmd = Command.getChannelMembers(currentUsername, currentServerName, channelName);
                    transfer();
                    ArrayList<String> members = (ArrayList<String>) data.getPrimary();
                    members.add("add new member to channel");

                    int option = inputHandler.showMenu(members);
                    if (option == members.size()){
                        String newMember = inputHandler.receiveData("type the username that you want to add to channel");
                        cmd = Command.addOneMemberToChannel(currentUsername,newMember, currentServerName,channelName);
                        transfer();
                        if (data.getKeyword().equals("checkNewChannel") && (boolean) data.getPrimary()){
                            inputHandler.printMsg("member successfully added to channel");
                        } else {
                            inputHandler.printMsg("something went wrong. try again later");
                        }
                    }
                }

            } while (action != 0);

        } while (choice != 0);
    }

    public void actionsList(String currentUsername){
        int action;
        do {
            // create a command to get user abilities in this server
            cmd = Command.getRole(currentUsername, currentServerName);
            transfer();
            Role role = (Role) data.getPrimary();
            ArrayList<String> actions = role.getAvailableAbilities();
            action = inputHandler.showMenu(actions);
            // create the command, for the keyword use the value in the abilities arrayList, to get the value use action - 1 index

        } while (action != 0);

    }

    private void transfer(){
        try {
            out.writeObject(cmd);
            data = (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e){
            inputHandler.printError(e);
        }
    }
}
