
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.nio.file.Files.readAllBytes;
import java.nio.file.Paths;
import java.util.*;

/**
 * holds user's current username
 * is responsible for : direct messages, settings , relationships, servers list and creating servers in the application
 */
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

    /**
     * shows the application's main menu to the user. including : setting, server list, relationships and direct messages.
     */
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

    /**
     * asks user whether to show new notifications  each time user enters application
     */
    public void getInbox() {
        String choice = inputHandler.receiveData("would you like to receive new messages?\n1) yes    2) no");
        if (choice.equals("1")) {
            cmd = Command.getNewMsgs(currentUsername);
            transfer();
            if (!data.getKeyword().equals("newMsgs")) {
                inputHandler.printMsg("unable to receive data from server");
            } else {
                ArrayList<Message> messages = (ArrayList<Message>) data.getPrimary();
                inputHandler.printMsg("INBOX");
                inputHandler.showMessages(messages, false);
                if (messages.size() == 0)
                    inputHandler.printMsg("no one cares about you, move on.");

            }
            cmd = Command.lastseenAll(currentUsername);
            transfer();
        }
    }

    /**
     * shows the list of user's servers.
     * user can choose to enter an existing server or create a new one
     */
    public void enterServersList() {
        int choice;
        do {
            // create a command to get a list of servers
            cmd = Command.userServers(currentUsername);
            transfer();
            ArrayList<String> serversList = new ArrayList<>();
            if (!data.getKeyword().equals("userServers")) {
                inputHandler.printMsg("unable to receive data from server");
            } else {
                serversList = (ArrayList<String>) data.getPrimary();
            }
            serversList.add("create new server");
            serversList.add("press 0 to exit");
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

    /**
     * receives necessary info for creating a new server - including server name, list of channels and members and roles- from user
     * and creates a command for creating new server and sends it to server side
     */
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
                System.out.println(role.getValue().getValues());
                cmd = Command.changeRole(currentUsername, role.getKey(), serverInfo.get(0).get(0), role.getValue());
                transfer();
            }
        }

    }

    /**
     * enters direct messages
     * user can choose a chat and start chatting with the other person or start a new chat with someone from their friend list
     */
    public void enterDirectMessages() {
        cmd = Command.getDirectChats(currentUsername);
        transfer();
        ArrayList<String> directChats = new ArrayList<>();

        if (!data.getKeyword().equals("directChats"))
            inputHandler.printMsg("unable to receive data from server");
        else
            directChats = (ArrayList<String>) data.getPrimary();
        directChats.add("start a new chat");
        directChats.add("press 0 to exit");

        while (true) {
            if (directChats.size() == 2){
                inputHandler.printMsg("you're miserable and have no one to talk to. sorry :(");
            }
            int choice = inputHandler.showMenu(directChats);
            if (choice == 0)
                break;
            // friends list is shown and user chooses a friend to start chat with
            if (choice == directChats.size() - 1){

                cmd  = Command.getFriends(currentUsername);
                transfer();
                ArrayList<String> friends = new ArrayList<>();
                if (!data.getKeyword().equals("friends"))
                    inputHandler.printMsg("unable to receive data from server");
                else
                    friends = (ArrayList<String>) data.getPrimary();
                friends.add("press 0 to exit");
                int chosenFriend;
                while (true){
                    if (friends.size() == 1)
                        inputHandler.printMsg("friend list is empty, you can first send friend request to someone and then start chatting");
                    chosenFriend = inputHandler.showMenu(friends);
                    if (chosenFriend == 0)
                        break;

                    String otherPerson = friends.get(chosenFriend - 1);
                    startPrivateChat(otherPerson);
                }
            }
            // user selects a direct chat and starts chatting
            else {
                startPrivateChat(directChats.get(choice - 1));
            }
        }
    }

    /**
     * creates a new thread for sending and receiving messages. also loads last 5 messages in the existing chats
     * @param otherPerson the other person which user wants to chat with
     */
    private void startPrivateChat(String otherPerson) {
        cmd = Command.tellPv(currentUsername, otherPerson);
        transfer();
        cmd = Command.getPvMsgs(currentUsername, otherPerson, 5);
        transfer();

        ArrayList<Message> recentMessages = new ArrayList<>();
        if (!data.getKeyword().equals("PvMsgs"))
            inputHandler.printMsg("unable to receive data from server");
        else {
            recentMessages = (ArrayList<Message>) data.getPrimary();
            inputHandler.printMsg("Recent Messages :");
            inputHandler.showMessages(recentMessages, true);
        }


        MessageReader messageReader = new MessageReader(in, inputHandler);
        MessageWriter messageWriter = new MessageWriter(out, currentUsername, otherPerson);


        messageReader.start();
        messageWriter.start();

        try {
            messageWriter.join();
            System.out.println("writer joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            messageReader.join();
            System.out.println("reader joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * opens the menu of different relationships including : friends, blocks and friend requests and calls the appropriaye method for each option
     */
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

    /**
     * opens the user's friends list
     * user can choose each friend, see their profile and block them if he wants. or can send a  friend request to someone
     */
    private void friendList() {
        int choice;
        // showing the list of friends
        do {
            // getting frind list from server
            cmd = Command.getFriends(currentUsername);
            transfer();

            // creating the list
            ArrayList<String> friends = new ArrayList<>();
            if (!data.getKeyword().equals("friends"))
                inputHandler.printMsg("unable to receive data from server");
            else
                friends = (ArrayList<String>) data.getPrimary();
            friends.add("send friend request");
            friends.add("press 0 to exit");

            // printing the list
            inputHandler.printMsg("your friends :");
            if (friends.size() == 2)
                inputHandler.printMsg("you're lonely and depressed. send a friend request for the love of god!");
            choice = inputHandler.showMenu(friends);

            // sending friend request
            if (choice == friends.size() - 1) {
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
                        cmd = Command.newRelation(friendRequest,currentUsername,friendUsername);
                        transfer();
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
                        cmd = Command.newRelation(block,currentUsername,friend.getUsername());
                        transfer();
                    }
                }
            }
        } while (choice != 0);
    }

    /**
     * shows user's block list and user can see each blocked person's profile
     */
    private void blocklist() {

        while (true){
            // getting the block list from server
            cmd = Command.getBlockList(currentUsername);
            transfer();

            // creating the list
            ArrayList<String> blocks = new ArrayList<>();
            if (!data.getKeyword().equals("blockList"))
                inputHandler.printMsg("unable to receive data from server");
            else
                blocks = (ArrayList<String>) data.getPrimary();
            blocks.add("press 0 to exit");

            // printing the list
            inputHandler.printMsg("your block list :");
            if (blocks.size() == 1){
                inputHandler.printMsg("block list is empty");
                return;
            }
            int choice = inputHandler.showMenu(blocks);
            if (choice == 0)
                break;
            cmd = Command.getUser(blocks.get(choice - 1));
            transfer();
            if (!data.getKeyword().equals("userInfo"))
                inputHandler.printMsg("something went worng");
            else {
                inputHandler.printMsg(data.getPrimary().toString());
                if (inputHandler.receiveData("1) unblock\npress 0 to exit").equals("1")){
                    cmd = Command.newRelation(Relationship.Rejected, currentUsername,blocks.get(choice - 1));
                    transfer();
                }
            }
        }
    }

    /**
     * shows all friend's requests to user. and user can see each persons profile and accept or reject their request
     */
    private void requestList() {
        while (true) {
            inputHandler.printMsg("new requests :");
            cmd = Command.getRequests(currentUsername);
            transfer();
            if (!data.getKeyword().equals("allFriendRequests")){
                inputHandler.printMsg("unable to receive data from server");
                return;
            }
            ArrayList<String> requests = (ArrayList<String>) data.getPrimary();
            requests.add("press 0 to exit");
            int choice = inputHandler.showMenu(requests);
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
                    cmd = Command.newRelation(result,currentUsername,user.getUsername());
                    transfer();
                }
            }
        }

    }

    /**
     * shows the application setting to user. user can change username or profile photo here
     */
    public void setting() {
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    press 0 to exit
                    1) set/change profile photo
                    2) change username""", 2);
            switch (choice) {
                case 1:
                    FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
                    dialog.setMode(FileDialog.LOAD);
                    dialog.setVisible(true);

                    String fileNameAndType = dialog.getFile();
                    String path = dialog.getDirectory()+"//"+dialog.getFile();

                    byte[] photo = new byte[0];
                    try {
                        photo = readAllBytes(Paths.get(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String[] splitName = fileNameAndType.split("\\.");

                    cmd = Command.changeProfilePhoto(currentUsername, photo, splitName[splitName.length - 1] );
                    try {
                        out.writeObject(cmd);
                    } catch (IOException e) {
                        inputHandler.printError(e);
                    }
                    inputHandler.printMsg("photo saved successfully");
                    break;
                case 2:
                    while (true) {
                        String newUsername = inputHandler.usernameValidation();
                        if (newUsername.equals("0"))
                            break;
                        // create a command to check if it exists
                        cmd = Command.changeUsername(currentUsername, newUsername);
                        transfer();
                        if (data.getKeyword().equals("checkChangeUsername") && (boolean)data.getPrimary()){
                            inputHandler.printMsg("username changed successfully");
                            currentUsername = newUsername;
                            break;
                        }
                        else
                            inputHandler.printMsg("this username already exists, please try another one");
                    }
                    break;
            }

        } while (choice != 0);
    }

    /**
     * transfers commands from client to server and datas from server to client
     */
    private void transfer() {
        try {
            out.writeObject(cmd);
            data = (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            inputHandler.printError(e);
        }
    }


}
