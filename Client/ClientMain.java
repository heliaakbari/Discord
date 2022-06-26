

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

public class ClientMain {

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static InputHandler inputHandler;
    private static String currentUsername;
    private static Command cmd;
    private static Data data;

    public static void main(String[] args) {

        int choice;
        inputHandler = new Console();

        try {
            socket = new Socket("localhost", 8642);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        do {
            choice = inputHandler.showMenu("""
                    1) signup
                    2) login
                    press 0 to exit""", 2);

            if (choice == 1 && signup() == 1 || choice == 2 && login() == 1) {
                Discord discord = new Discord(currentUsername, out, in);
                discord.startDiscord();
            }
        } while (choice != 0);

        //here you must exit the program for client, close the connection
    }


    // when the login is successful and returns 1, you should communicate with server and set the client handler id
    public static int login() {
        while (true) {
            ArrayList<String> info = inputHandler.login();
            if (info == null)
                return 0;
            else {
                // create command
                // if found the condition is true
                cmd = Command.getUser(info.get(0));
                try {
                    out.writeObject(cmd);
                    data = (Data) in.readObject();
                } catch (IOException | ClassNotFoundException e){
                    inputHandler.printError(e);
                }
                User user = (User) data.getPrimary();
                if (user.getPassword().equals(info.get(1)) && data.getUser().equals(info.get(0))) {
                    currentUsername = info.get(0);
                    return 1;
                } else
                    inputHandler.printMsg("incorrect username or password, try again!\npress 0 to exit");
            }
        }
    }

    public static int signup() {

        while (true) {
            String input = inputHandler.usernameValidation();
            if (input.equals("0"))
                return 0;
            else {
                // create command, if not found condition is true
                cmd = Command.getUser(input);
                try{
                    out.writeObject(cmd);
                    data = (Data) in.readObject();
                } catch (IOException | ClassNotFoundException e){
                    inputHandler.printError(e);
                }

                if (data == null) {
                    currentUsername = input;
                    break;
                } else
                    inputHandler.printMsg("this username already exists please try another one.");
            }
        }

        User userInfo = inputHandler.signup();
        if (userInfo == null)
            return 0;
        else {
            // command to add new user to database
            userInfo.setUsername(currentUsername);
            cmd = Command.newUser(userInfo);
            try {
                out.writeObject(cmd);
                data = (Data) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                inputHandler.printError(e);
            }
            if ((boolean) data.getPrimary())
                inputHandler.printMsg("successfully signed up.");
            return 1;
        }
    }

    //    public static void startDiscord() {
//        getInbox();
//        int choice;
//        do {
//            choice = inputHandler.showMenu("""
//                    1) servers list
//                    2) direct messages
//                    3) relationships list
//                    4) account setting
//                    press 0 to exit""", 4);
//
//            switch (choice) {
//                case 1 -> chooseServer();
//                case 2 -> enterDirectMessages();
//                case 3 -> enterRelationshipsList();
//                case 4 -> setting();
//            }
//
//        } while (choice != 0);
//    }

//    public static void chooseServer() {
//
//        // create a command to get a list of servers
//        ArrayList<String> serversList;
//        serversList.add("create new server");
//        serversList.add("press 0 to exit");
//
//        int choice;
//        do {
//            choice = inputHandler.showMenu(serversList);
//            // user can choose to create a new server
//            if (choice == serversList.size() - 1){
//                createServer();
//            }
//            // or enter an existing server and do some actions there
//            else {
//                currentServer = serversList.get(choice - 1);
//                int action;
//                do {
//                    // create a command to get user abilities in this server
//                    cmd = Command.getRole(currentUsername, currentServer);
//                    try {
//                        out.writeObject(cmd);
//                        data = (Data) in.readObject();
//                    } catch (IOException | ClassNotFoundException e){
//                        inputHandler.printError(e);
//                    }
//                    Role role = (Role) data.getPrimary();
//                    ArrayList<String> actions = role.getAvailableAbilities();
//                    actions.add("add member to channel");
//                    action = inputHandler.showMenu(actions);
//                    // create the command, for the keyword use the value in the abilities arrayList, to get the value use action - 1 index
//
//                } while (action != 0);
//                chooseChannel(serversList.get(choice));
//            }
//
//        } while (choice != 0);
//    }

//    public static void createServer(){}


//    public static void chooseChannel(String server) {
//        // create a command to get a list of channels in this server
//        ArrayList<String> channelsList;
//        channelsList.add("press 0 to exit");
//        int choice;
//        do {
//            choice = inputHandler.showMenu(channelsList);
//
//            int action;
//            do {
//                action = inputHandler.showMenu("1) start chatting\n2) see the channel members", 2);
//                if (action == 1) {
//                    inputHandler.printMsg("you can type your messages now, to react to a message type the message number and then your reaction.");
//                    // transfer messages with server
//                } else if (action == 2) {
//                    // creat a command to get the members
//                }
//            } while (action != 0);
//
//        } while (choice != 0);
//    }

//    public static void enterRelationshipsList() {
//        int choice;
//        do {
//            choice = inputHandler.showMenu("""
//                    1) friends list
//                    2) blocked list
//                    3) pending list
//                    4) send friend request
//                    5) block someone
//                    press 0 to exit""", 5);
//
//            // create appropriate command based on each option
//            switch (choice) {
//                case 1 -> ;
//                case 2 -> ;
//                case 3 -> ;
//                case 4 -> ;
//                case 5 -> ;
//            }
//        } while (choice != 0);
//    }

//    public static void enterDirectMessages() {
//
//    }


//    public static void setting() {
//        int choice;
//        do {
//            choice = inputHandler.showMenu("""
//                    press 0 to exit
//                    1) set/change profile photo
//                    2) change username""", 2);
//            switch (choice) {
//                case 1:
//                    JFileChooser fileChooser = new JFileChooser();
//                    fileChooser.setCurrentDirectory(new File(System.getProperty("c:\\")));
//                    int result = fileChooser.showOpenDialog(null);
//                    if (result == JFileChooser.APPROVE_OPTION) {
//                        File selectedFile = fileChooser.getSelectedFile();
//                        try {
//                            byte[] photo = Files.readAllBytes(selectedFile.toPath());
//                            String fileType = fileChooser.getTypeDescription(selectedFile);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    // create a command to set profile photo
//                    break;
//                case 2 :
//                    while (true) {
//                        String newUsername = inputHandler.usernameValidation();
//                        if (newUsername.equals("0"))
//                            break;
//                        // create a command to check if it exists if not found the condition is true
//                        if (condition){
//                            // create a command to set the new username
//                            break;
//                        }
//                    }
//                    break;
//            }
//
//        } while (choice != 0);
//    }

}
