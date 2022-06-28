import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.*;

public class Discord {

    private String currentUsername;
    private InputHandler inputHandler;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Command cmd;
    private Data data;

    public Discord(String username, ObjectOutputStream out, ObjectInputStream in) {
        currentUsername = username;
        inputHandler = new Console();
        this.out = out;
        this.in = in;
    }

    public void startDiscord() {
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

    public void getInbox() {
        cmd = Command.getNewMsgs(currentUsername);
        transfer();
        if (!data.getKeyword().equals("newMsgs")) {
            inputHandler.printMsg("unable to receive data from server");
        } else {
            ArrayList<Message> messages = (ArrayList<Message>) data.getPrimary();
            inputHandler.printMsg("INBOX");
            inputHandler.showMessages(messages);
        }
        cmd = Command.lastseenAll(currentUsername);
        transfer();

    }

    public void enterServersList() {

        // create a command to get a list of servers
        cmd = Command.userServers(currentUsername);
        transfer();
        ArrayList<String> serversList = new ArrayList<>();
        if (!data.getKeyword().equals("userServers")) {
            inputHandler.printMsg("unable to receive data from server");
        } else {
            serversList = (ArrayList<String>) data.getPrimary();
            serversList.add("create new server");
            serversList.add("press 0 to exit");
        }

        int choice;
        do {
            choice = inputHandler.showMenu(serversList);
            // user can choose to create a new server
            if (choice == serversList.size() - 1) {
                createServer();
            }
            // or enter an existing server and do some actions there
            else if (choice != 0){
                String currentServer = serversList.get(choice - 1);
                Server server = new Server(currentServer, out, in);
                server.enterServer(currentUsername);
            }

        } while (choice != 0);

    }

    public void createServer() {
        // getting all necessary info for creating server
        ArrayList<ArrayList<String>> serverInfo = inputHandler.createServer();

        // creating server
        cmd = Command.newServer(currentUsername, serverInfo.get(0).get(0));
        transfer();
        if (data.getKeyword().equals("checkNewServer") && !(boolean)data.getPrimary()){
            inputHandler.printMsg("oops! couldn't create server. try again later");
            return;
        }
        // adding channels
        for (String channelName : serverInfo.get(1)) {
            cmd = Command.newChannel(currentUsername, serverInfo.get(0).get(0), channelName);
            transfer();
            if (data.getKeyword().equals("checkNewChannel") && !(boolean)data.getPrimary())
                inputHandler.printMsg("couldn't create channel!");
        }
        // adding people
        cmd = Command.addPeopleToServer(currentUsername, serverInfo.get(0).get(0), serverInfo.get(2));
        transfer();

        // defining roles
        String choice = inputHandler.receiveData("would you like to define any role for this server?\n1) yes    2) no");
        if (choice.equals("1")) {
            HashMap<String, Role> roles = inputHandler.defineRoles();
            for (Map.Entry<String, Role> role : roles.entrySet()) {
                cmd = Command.changeRole(currentUsername, role.getKey(), serverInfo.get(0).get(0), role.getValue());
                transfer();
            }
        }

    }

    public void enterDirectMessages() {
        cmd = Command.getDirectChats(currentUsername);
        transfer();
        ArrayList<String> directChats = new ArrayList<>();

        if (!data.getKeyword().equals("directChats"))
            inputHandler.printMsg("unable to receive data from server");
        else
            directChats = (ArrayList<String>) data.getPrimary();
        directChats.add("press 0 to exit");


        while (true) {
            int choice = inputHandler.showMenu(directChats);
            if (choice == 0)
                break;

            cmd = Command.tellPv(currentUsername, directChats.get(choice - 1));
            transfer();
            cmd = Command.getPvMsgs(currentUsername, directChats.get(choice - 1), 10);
            transfer();

            ArrayList<Message> recentMessages = new ArrayList<>();
            if (!data.getKeyword().equals("PvMsgs"))
                inputHandler.printMsg("unable to receive data from server");
            else
                recentMessages = (ArrayList<Message>) data.getPrimary();

            inputHandler.printMsg("Recent Messages :");
            inputHandler.showMessages(recentMessages);

            MessageReader messageReader = new MessageReader(in, inputHandler);
            messageReader.start();

            MessageWriter messageWriter = new MessageWriter(out, currentUsername, directChats.get(choice - 1));
            messageWriter.start();

            if (!messageReader.isAlive() && !messageWriter.isAlive()) {
                cmd = Command.lastseenPv(currentUsername, directChats.get(choice - 1));
                transfer();
            }
        }


    }

    public void enterRelationshipsList() {
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    1) friends list
                    2) block list
                    3) Requests list
                    press 0 to exit""", 5);

            // create appropriate command based on each option
            switch (choice) {
                case 1 -> friendList();
                case 2 -> blocklist();
                case 3 -> requestList();
            }
        } while (choice != 0);
    }

    private void friendList() {
        cmd = Command.getFriends(currentUsername);
        transfer();
        ArrayList<String> friends = new ArrayList<>();
        if (!data.getKeyword().equals("friends"))
            inputHandler.printMsg("unable to receive data from server");
        else
            friends = (ArrayList<String>) data.getPrimary();
        friends.add("send friend request");
        int choice;

        // showing the list of friends
        do {
            choice = inputHandler.showMenu(friends);

            // sending friend request
            if (choice == friends.size()) {
                String friendUsername;

                while (true) {
                    friendUsername = inputHandler.receiveData("type the username");
                    if (friendUsername.equals("0"))
                        break;
                    cmd = Command.getUser(friendUsername);
                    transfer();
                    if (data.getKeyword().equals("userInfo") && data.getPrimary() == null) {
                        inputHandler.printMsg("there is no such user with this username!");
                    } else {
                        Relationship friendRequest = Relationship.Friend_pending;
                        friendRequest.setSender(currentUsername);
                        friendRequest.setReceiver(friendUsername);
                        cmd = Command.newRelation(friendRequest);
                        transfer();
                        if (data.getKeyword().equals("checkNewRelation") && (boolean) data.getPrimary()) {
                            inputHandler.printMsg("request sent");
                        } else {
                            inputHandler.printMsg("something went wrong, try again later");
                        }
                        break;
                    }
                }
            }

            // showing the chosen friend
            else if (choice != 0){
                cmd = Command.getUser(friends.get(choice - 1));
                transfer();
                if (!data.getKeyword().equals("userInfo")){
                    inputHandler.printMsg("unable to receive data from server");
                }
                else {
                    User friend = (User) data.getPrimary();
                    if (inputHandler.receiveData(friend, "0) back\n1) block this person", 1) == 1) {
                        Relationship block = Relationship.Block;
                        block.setSender(currentUsername);
                        block.setReceiver(friend.getUsername());
                        cmd = Command.newRelation(block);
                        transfer();
                        if (!data.getKeyword().equals("checkNewRelation") ||!(boolean) data.getPrimary()) {
                            inputHandler.printMsg("something went wrong, try again later");
                        }
                    }
                }
            }

        } while (choice != 0);
    }

    private void blocklist() {
        cmd = Command.getBlockList(currentUsername);
        transfer();
        ArrayList<String> blocks = new ArrayList<>();
        if (!data.getKeyword().equals("userServers"))
            inputHandler.printMsg("unable to receive data from server");
        else
            blocks = (ArrayList<String>) data.getPrimary();

        blocks.add("press 0 to exit");
        inputHandler.showMenu(blocks);
    }

    private void requestList() {
        cmd = Command.getRequests(currentUsername);
        transfer();
        if (data.getKeyword().equals("allFriendRequests")){
            inputHandler.printMsg("unable to receive data from server");
            return;
        }
        ArrayList<String> requests = (ArrayList<String>) data.getPrimary();
        int choice;
        while (true) {
            choice = inputHandler.showMenu(requests);
            if (choice == 0)
                break;
            cmd = Command.getUser(requests.get(choice - 1));
            transfer();
            if (data.getKeyword().equals("userInfo")) {
                User user = (User) data.getPrimary();
                int decision = inputHandler.receiveData(user, "1) accept    2) reject\npress 0 to exit", 2);
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

    }

    public void setting() {
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
                    cmd = Command.changeProfilePhoto(currentUsername, photo, fileType);
                    try {
                        out.writeObject(cmd);
                    } catch (IOException e) {
                        inputHandler.printError(e);
                    }
                    break;
                case 2:
                    while (true) {
                        String newUsername = inputHandler.usernameValidation();
                        if (newUsername.equals("0"))
                            break;
                        // create a command to check if it exists
                        cmd = Command.getUser(newUsername);
                        transfer();
                        if (data.getKeyword().equals("userInfo") && data.getPrimary() == null) {
                            // create a command to set the new username
                            cmd = Command.changeUsername(currentUsername, newUsername);
                            transfer();
                            currentUsername = newUsername;
                            break;
                        }
                    }
                    break;
            }

        } while (choice != 0);
    }

    private void transfer() {
        try {
            out.writeObject(cmd);
            data = (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            inputHandler.printError(e);
        }
    }


}
