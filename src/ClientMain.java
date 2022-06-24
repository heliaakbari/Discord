
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientMain {

    static Socket socket;
    static ObjectOutputStream out;
    static ObjectInputStream in;
    static InputHandler inputHandler;

    public static void main(String[] args) {

        int choice = -1;
        inputHandler = new Console();

        try {
            socket = new Socket("localhost", 8642);
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        do {
            choice = inputHandler.showMenu("""
                    1) signup
                    2) login
                    press 0 to exit""", 2);

            if (choice == 1 && signup() == 1  || choice == 2 && login() == 1) {
                startDiscord();
            }
        } while (choice != 0);

        //here you must exit the program for client, close the connection
    }


    // when the login is successful and returns 1, you should communicate with server and set the client handler id
    public static int login(){
        while (true){
            String input = inputHandler.login();
            if (input.equals("0"))
                return 0;
            else {
                // create command
                // if found the condition is true
                if (condition)
                    return 1;
                else
                    inputHandler.printMsg("incorrect username or password, try again!");
            }
        }
    }


    public static int signup(){
        String username;
        while (true){
            username = inputHandler.usernameValidation();
            if (username.equals("0"))
                return 0;
            else {
                String userInfo = inputHandler.signup();
                // create command, if not found condition is true
                if (condition){
                    return 1;
                }
                else
                    inputHandler.printMsg("this username already exists please try another one.");

            }
        }
    }


    public static void startDiscord(){
        getInbox();
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    1) servers list
                    2) direct messages
                    3) friends list
                    4) blocked list
                    5) pending list
                    press 0 to exit""", 5);

            switch (choice){
                case 1 -> chooseServer();
                case 2 -> enterDirectMessages();
                case 3 -> enterFriendList();
                case 4 -> enterBlockList();
                case 5 -> enterPendingList();
            }
        } while (choice != 0);
    }

    public static void chooseServer(){
        // create a command to get a list of servers and pass the list to printmsg in inputhandler and ask user to choose one
        ArrayList<String> serversList;
        serversList.add("press 0 to exit");
        int choice;
        do{
            choice = inputHandler.showMenu(serversList.toString(), serversList.size() - 1);
            chooseChannel(serversList.get(choice));

        } while (choice != 0);
    }
    public static void chooseChannel(String server){
        // create a command to get a list of channels in this server
        ArrayList<String> channelsList;
        channelsList.add("press 0 to exit");
        int choice;
        do {
            choice = inputHandler.showMenu(channelsList.toString(), channelsList.size() - 1) ;

            int action;
            do {
                action = inputHandler.showMenu("1) start chatting\n2) see the channel members", 2);
                if (action == 1){
                    inputHandler.printMsg("you can type your messages now, to react to a message type the message number and then your reaction.");
                    // transfer messages with server
                } else if (action == 2){
                    // creat a command to get the members
                }
            } while (action != 0);

        } while (choice != 0);
    }

    public static void enterFriendList(){
        // create a command to get friends list
        ArrayList<User> friends;
        int friend;
        do{
            inputHandler.printMsg("press 0 to exit");
            friend = inputHandler.showMenu(friends.toString(), friends.size());
            if (friend != 0){
                int action = inputHandler.showMenu("0) back to friends list\n1) block this account", 1);
                if (action == 1){
                    // creat a block command
                }
            }
        } while (friend != 0);




    }
    public static void enterBlockList(){}
    public static void enterPendingList(){}
    public static void enterDirectMessages(){}

    public static void getInbox(){} // this method is called inside startDiscord method


}
