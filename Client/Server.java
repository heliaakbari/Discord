import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * holds the name of the server which the user is currently in.
 * is responsible for : showing the list of channels and actions inside the server, showing the list of members
 * and actions inside each channel, handling channel chats and ...
 */
public class Server {

    private String currentServerName;
    private InputHandler inputHandler;
    private ObjectOutputStream out;
    private ObjectOutputStream fout;
    private ObjectInputStream in;
    private ObjectInputStream fin;
    private Command cmd;
    private Data data;

    public Server(String name, ObjectOutputStream out, ObjectOutputStream fout, ObjectInputStream in, ObjectInputStream fin) {
        currentServerName = name;
        inputHandler = new Console();
        this.out = out;
        this.in = in;
        this.fout = fout;
        this.fin = fin;
    }

    /**
     * shows two options for seeing the list of  actions or channels once user enters the server
     *
     * @param currentUsername username of the user who enters the server
     */
    public void enterServer(String currentUsername) {
        int choice;
        do {
            choice = inputHandler.showMenu("1) actions list\n2) channels list\npress 0 to exit ", 2);
            if (choice == 1) {
                if (actionsList(currentUsername) == -1)
                    break;
            } else if (choice == 2)
                channelsList(currentUsername);

        } while (choice != 0);
    }

    /**
     * shows the list of channels, once user enters a channel shows the list of available actions inside that channels
     * including chatting, seeing the member's list, seeing the pin bar and leaving the channel
     *
     * @param currentUsername
     */
    public void channelsList(String currentUsername) {

        int choice;
        do {
            // create a command to get a list of channels in this server
            cmd = Command.userChannels(currentUsername, currentServerName);
            transfer();
            if (!data.getKeyword().equals("userChannels")) {
                inputHandler.printMsg("unable to receive data from server");
                return;
            }
            ArrayList<String> channelsList = (ArrayList<String>) data.getPrimary();
            channelsList.add("press 0 to exit");
            String channelName;
            if (channelsList.size() == 1)
                inputHandler.printMsg("channel list is empty");
            choice = inputHandler.showMenu(channelsList);
            if (choice == 0)
                break;
            // user chooses a channel to enter
            channelName = channelsList.get(choice - 1);

            int action;
            do {
                // list of actions inside channel
                action = inputHandler.showMenu("1) start chatting\n2) see the channel members\n3) see pin bar\n4) leave channel\npress 0 to exit", 4);

                // chatting
                if (action == 1) {
                    inputHandler.printMsg("you can type your messages now, to react to a message type 'react <message number> <reaction> '.\n" +
                            "to send a file type 'send file'. to download a file type 'open file <file name>'. press 0 to exit the chat");
                    cmd = Command.tellChannel(currentUsername, currentServerName, channelName);
                    transfer();

                    // transfer messages with server

                    cmd = Command.getChannelMsgs(currentUsername, currentServerName, channelName, 5);
                    transfer();
                    if (!data.getKeyword().equals("channelMsgs")) {
                        inputHandler.printMsg("unable to receive data from server");
                        return;
                    }
                    ArrayList<Message> messageNumbering = (ArrayList<Message>) data.getPrimary();
                    inputHandler.showMessages(messageNumbering, true);
                    MessageReader messageReader = new MessageReader(in, inputHandler, messageNumbering, currentUsername);

                    MessageWriter messageWriter = new MessageWriter(out, fout, fin, new ArrayList<>(Arrays.asList(currentUsername, channelName, currentServerName)), messageNumbering);
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
                // member's list
                else if (action == 2) {
                    // creat a command to get the members
                    cmd = Command.getChannelMembers(currentUsername, currentServerName, channelName);
                    transfer();
                    if (!data.getKeyword().equals("channelMembers")) {
                        inputHandler.printMsg("unable to receive data from server");
                        action = 0;
                    } else {
                        ArrayList<String> members = (ArrayList<String>) data.getPrimary();
                        members.add("add new member to channel");
                        members.add("press 0 to exit");

                        int option = inputHandler.showMenu(members);
                        if (option == members.size() - 1) {
                            String newMember = inputHandler.receiveData("type the username that you want to add to channel");
                            cmd = Command.addOneMemberToChannel(currentUsername, newMember, currentServerName, channelName);
                            transfer();
                            inputHandler.printMsg("member successfully added to channel");

                        }
                    }
                }
                // pin bar
                else if (action == 3) {
                    cmd = Command.getPinnedMsgs(currentUsername, currentServerName, channelName);
                    transfer();
                    if (data.getKeyword().equals("pinnedMsgs")) {
                        ArrayList<Message> pinnedMessages = (ArrayList<Message>) data.getPrimary();
                        inputHandler.printMsg("Pinned Messages :");
                        inputHandler.showMessages(pinnedMessages, true);
                        if (pinnedMessages.size() == 0)
                            inputHandler.printMsg("no pinned message available");
                    }

                }
                // leaving channel
                else if (action == 4) {
                    cmd = Command.banFromChannel(currentUsername, currentServerName, channelName);
                    transfer();
                    action = 0;
                }

            } while (action != 0);

        } while (choice != 0);
    }

    /**
     * shows the list of possible actions for the user based on their role in this channel
     *
     * @param currentUsername
     * @return
     */
    public int actionsList(String currentUsername) {
        int action;
        do {
            // create a command to get user abilities in this server
            cmd = Command.getRole(currentUsername, currentServerName);
            transfer();
            if (!data.getKeyword().equals("role")) {
                inputHandler.printMsg("unable to receive data from server");
                return 0;
            }

            Role role = (Role) data.getPrimary();
            ArrayList<String> actions = role.getAvailableAbilities();
            actions.add("leave server");
            actions.add("press 0 to exit");

            action = inputHandler.showMenu(actions);
            if (action == 0)
                break;
            String chosenAction = actions.get(action - 1);
            switch (chosenAction) {
                case "create channel":
                    String newChannel = inputHandler.receiveData("enter channel name");
                    cmd = Command.newChannel(currentUsername, currentServerName, newChannel);
                    transfer();
                    if (data.getKeyword().equals("checkNewChannel") && !(boolean) data.getPrimary())
                        inputHandler.printMsg("oops! couldn't create channel. try again later.");
                    String newMember;
                    while (true) {
                        newMember = inputHandler.receiveData("enter the usernames you want to add to the channel. ad as always, press 0 when finished");
                        if (newMember.equals("0"))
                            break;
                        cmd = Command.addOneMemberToChannel(currentUsername, newMember, currentServerName, newChannel);
                        transfer();
                    }
                    break;
                case "remove channel":
                    String removableChannel = inputHandler.receiveData("enter channel name to be deleted");
                    cmd = Command.deleteChannel(currentUsername, currentServerName, removableChannel);
                    transfer();
                    break;
                case "remove member":
                    String person = inputHandler.receiveData("who do you want  to remove?");
                    cmd = Command.banFromServer(person, currentServerName);
                    transfer();
                    break;
                case "restrict member":
                    person = inputHandler.receiveData("type the username you want to ban");
                    String channel = inputHandler.receiveData("type the channel you want to ban this person from");
                    cmd = Command.banFromChannel(person, currentServerName, channel);
                    transfer();
                    break;
                case "ban member":
                    person = inputHandler.receiveData("type the username you want to ban from server");
                    cmd = Command.banFromServer(person, currentServerName);
                    transfer();
                    break;
                case "change sever name":
                    String newName = inputHandler.receiveData("type the new name for the server");
                    cmd = Command.changeServerName(currentUsername, currentServerName, newName);
                    transfer();
                    currentServerName = newName;
                    break;
                case "see chat history":
                    chatHistory(currentUsername);
                    break;
                case "pin message":
                    Message message = chatHistory(currentUsername);
                    if (message != null) {
                        cmd = Command.pinMsg(currentUsername, message);
                        transfer();
                    }
                    break;
                case "delete server":
                    cmd = Command.deleteServer(currentUsername, currentServerName);
                    transfer();
                    return -1;
                case "leave server":
                    cmd = Command.banFromServer(currentUsername, currentServerName);
                    transfer();
                    return -1;
            }

        } while (action != 0);

        return 0;
    }

    /**
     * shows the chat history of the channel that user chooses
     *
     * @param currentUsername
     * @return te message that user chooses from the history
     */
    private Message chatHistory(String currentUsername) {
        String channel = inputHandler.receiveData("type a channel name to see chat history");

        cmd = Command.getChannelMsgs(currentUsername, currentServerName, channel, 10);
        transfer();
        if (!data.getKeyword().equals("channelMsgs")) {
            inputHandler.printMsg("unable to receive data from server");
            return null;
        }
        ArrayList<Message> recentMessages = (ArrayList<Message>) data.getPrimary();
        StringBuilder stringBuilder = new StringBuilder();
        for (Message message : recentMessages) {
            stringBuilder.append(recentMessages.indexOf(message) + 1).append(") ").append(message.shortFormToString()).append("    ").append(message.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        }
        stringBuilder.append("press 0 to exit");
        int messageNum = inputHandler.showMenu(stringBuilder.toString(), recentMessages.size());
        if (messageNum == 0)
            return null;
        else
            return recentMessages.get(messageNum - 1);
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
