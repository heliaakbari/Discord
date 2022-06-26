import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Server {

    private  String currentServerName;
    private  InputHandler inputHandler;
    private  ObjectOutputStream out;
    private  ObjectInputStream in;
    private  Command cmd;
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
                 channelsList();

        } while (choice != 0);

    }

    public void channelsList(){
        // create a command to get a list of channels in this server
        ArrayList<String> channelsList;
        channelsList.add("press 0 to exit");
        int choice;
        do {
            choice = inputHandler.showMenu(channelsList);

            int action;
            do {
                action = inputHandler.showMenu("1) start chatting\n2) see the channel members\npress 0 to exit", 2);
                if (action == 1) {
                    inputHandler.printMsg("you can type your messages now, to react to a message type the message number and then your reaction.");
                    // transfer messages with server
                } else if (action == 2) {
                    // creat a command to get the members
                    // also remember to add "add new member to channel" at the end of the list of members
                }
            } while (action != 0);

        } while (choice != 0);
    }

    public void actionsList(String currentUsername){
        int action;
        do {
            // create a command to get user abilities in this server
            cmd = Command.getRole(currentUsername, currentServerName);
            try {
                out.writeObject(cmd);
                data = (Data) in.readObject();
            } catch (IOException | ClassNotFoundException e){
                inputHandler.printError(e);
            }
            Role role = (Role) data.getPrimary();
            ArrayList<String> actions = role.getAvailableAbilities();
            action = inputHandler.showMenu(actions);
            // create the command, for the keyword use the value in the abilities arrayList, to get the value use action - 1 index

        } while (action != 0);

    }
}
