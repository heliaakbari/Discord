import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public class Discord {

    private String currentUsername;
    private InputHandler inputHandler;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Command cmd;
    private Data data;

    public Discord(String username, ObjectOutputStream out, ObjectInputStream in){
        currentUsername = username;
        inputHandler = new Console();
        this.out = out;
        this.in = in;
    }

    public void startDiscord(){
        getInbox();
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    1) servers list
                    2) direct messages
                    3) relationships list
                    4) account setting
                    press 0 to exit""", 4);

            switch (choice) {
                case 1 -> enterServersList();
                case 2 -> enterDirectMessages();
                case 3 -> enterRelationshipsList();
                case 4 -> setting();
            }

        } while (choice != 0);
    }

    public void getInbox(){
        cmd = Command.getNewMsgs(currentUsername);
        try {
            out.writeObject(cmd);
            data = (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e){
            inputHandler.printError(e);
        }
        ArrayList<Message> messages = (ArrayList<Message>) data.getPrimary();

        // creating a list of messages and passing it to inputHandler to be printed
        StringBuilder stringBuilder = new StringBuilder("INBOX");
        stringBuilder.append("=============================================================================================\n");
        for (Message message : messages) {
            stringBuilder.append(message);
        }
        stringBuilder.append("=============================================================================================\n");
        inputHandler.printMsg(stringBuilder.toString());
    }

    public void enterServersList(){

        // create a command to get a list of servers
        cmd = Command.userServers(currentUsername);
        try {
            out.writeObject(cmd);
            data = (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e){
            inputHandler.printError(e);
        }
        ArrayList<String> serversList = (ArrayList<String>) data.getPrimary();
        serversList.add("create new server");
        serversList.add("press 0 to exit");

        int choice;
        do {
            choice = inputHandler.showMenu(serversList);
            // user can choose to create a new server
            if (choice == serversList.size() - 1){
                createServer();
            }
            // or enter an existing server and do some actions there
            else {
                String currentServer = serversList.get(choice - 1);
                Server server = new Server(currentServer, out, in);
                server.enterServer(currentUsername);
            }

        } while (choice != 0);

    }

    public void createServer(){

    }

    public void enterDirectMessages(){

    }

    public void enterRelationshipsList(){
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    1) friends list
                    2) block list
                    3) Requests list
                    press 0 to exit""", 5);

            // create appropriate command based on each option
            switch (choice){
                case 1 -> friendList();
                case 2 -> blocklist();
                case 3 -> requestList();
            }
        } while (choice != 0);
    }

    private void friendList(){
        cmd = Command.getFriends(currentUsername);
        transfer();
        ArrayList<String> friends = (ArrayList<String>) data.getPrimary();
        friends.add("send friend request");
        int choice;

        // showing the list of friends
        do {
            choice = inputHandler.showMenu(friends);

            // sending friend request
            if (choice == friends.size()){
                String friendUsername;

                while (true){
                    friendUsername = inputHandler.friendRequest();
                    if (friendUsername.equals("0"))
                        break;
                    cmd = Command.getUser(friendUsername);
                    transfer();
                    if (data.getPrimary() == null){
                        inputHandler.printMsg("there is no such user with this username!");
                    }
                    else {
                        Relationship friendRequest = Relationship.Friend_pending;
                        friendRequest.setSender(currentUsername);
                        friendRequest.setReceiver(friendUsername);
                        cmd = Command.newRelation(friendRequest);
                        transfer();
                        if ((boolean)data.getPrimary()){
                            inputHandler.printMsg("request sent");
                        } else {
                            inputHandler.printMsg("something went wrong, try again later");
                        }
                        break;
                    }
                }
            }

            // showing the chosen friend
            else {
                cmd = Command.getUser(friends.get(choice - 1));
                transfer();
                User friend = (User) data.getPrimary();
                if (inputHandler.showFriendInfo(friend)){
                    Relationship block = Relationship.Block;
                    block.setSender(currentUsername);
                    block.setReceiver(friend.getUsername());
                    cmd = Command.newRelation(block);
                    transfer();
                    if (!(boolean)data.getPrimary()){
                        inputHandler.printMsg("something went wrong, try again later");
                    }
                }
            }

        } while (choice != 0);
    }

    private void blocklist(){
        cmd = Command.getBlockList(currentUsername);
        transfer();
        ArrayList<String> blocks = (ArrayList<String>) data.getPrimary();
        blocks.add("press 0 to exit");
        inputHandler.showMenu(blocks);
    }

    private void requestList(){
        cmd = Command.getRequests(currentUsername);
        transfer();
        ArrayList<String> requests = (ArrayList<String>) data.getPrimary();
        int choice;
        while (true){
            choice = inputHandler.showMenu(requests);
            if (choice == 0)
                break;
            cmd = Command.getUser(requests.get(choice - 1));
            transfer();
            User user = (User) data.getPrimary();
            int decision = inputHandler.showRequest(user);
            if (decision != 0) {
                Relationship result = null;
                if (decision == 1) {
                    result = Relationship.Friend;
                } else if (decision == 2) {
                    result = Relationship.Rejected;
                }
                cmd = Command.newRelation(result);
                transfer();
            }
        }

    }


    public void setting(){
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    press 0 to exit
                    1) set/change profile photo
                    2) change username""", 2);
            switch (choice) {
                case 1:
                    byte[] photo = null;
                    String fileType = null;
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("c:\\")));
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        try {
                            photo = Files.readAllBytes(selectedFile.toPath());
                            fileType = fileChooser.getTypeDescription(selectedFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // create a command to set profile photo
                    cmd = Command.changeProfilePhoto(currentUsername,photo, fileType);
                    try {
                        out.writeObject(cmd);
                    } catch (IOException e){
                        inputHandler.printError(e);
                    }
                    break;
                case 2 :
                    while (true) {
                        String newUsername = inputHandler.usernameValidation();
                        if (newUsername.equals("0"))
                            break;
                        // create a command to check if it exists if not found the condition is true
                        cmd = Command.getUser(newUsername);
                        transfer();
                        if (data.getPrimary() == null){
                            // create a command to set the new username
                            cmd = Command.changeUsername(currentUsername, newUsername);
                            currentUsername = newUsername;
                            break;
                        }
                    }
                    break;
            }

        } while (choice != 0);
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
