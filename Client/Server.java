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

    public Server(String name, ObjectOutputStream out, ObjectInputStream in) {
        currentServerName = name;
        inputHandler = new Console();
        this.out = out;
        this.in = in;
    }

    public void enterServer(String currentUsername) {
        int choice;
        do {
            choice = inputHandler.showMenu("1) actions list\n2) channels list\npress 0 to exit ", 2);
            if (choice == 1) {
                if (actionsList(currentUsername) == -1)
                    break;
            }
            else if (choice == 2)
                    channelsList(currentUsername);

        } while (choice != 0);

    }

    public void channelsList(String currentUsername) {

        // create a command to get a list of channels in this server
        cmd = Command.userChannels(currentUsername, currentServerName);
        transfer();
        if (!data.getKeyword().equals("userChannels")){
            inputHandler.printMsg("unable to receive data from server");
            return;
        }
        ArrayList<String> channelsList = (ArrayList<String>) data.getPrimary();
        channelsList.add("press 0 to exit");
        int choice;
        String channelName;

        do {
            choice = inputHandler.showMenu(channelsList);
            if (choice == 0)
                break;
            channelName = channelsList.get(choice - 1);

            int action;
            do {
                action = inputHandler.showMenu("1) start chatting\n2) see the channel members\n3) see pin bar\npress 0 to exit", 3);

                if (action == 1) {
                    inputHandler.printMsg("you can type your messages now, to react to a message type 'react <message number> <reaction> '.\n" +
                            "to send a file type 'send file'. to download a file type 'open file <file name>'. press 0 to exit the chat");
                    cmd = Command.tellChannel(currentUsername, currentServerName, channelName);
                    transfer();

                    // transfer messages with server

                    ArrayList<Message> messageNumbering = new ArrayList<>();
                    MessageReader messageReader = new MessageReader(in, inputHandler, messageNumbering, currentUsername);
                    messageReader.start();

                    MessageWriter messageWriter = new MessageWriter(out, new ArrayList<>(Arrays.asList(currentUsername, channelName, currentServerName)), messageNumbering);
                    messageWriter.start();

                    if (!messageReader.isAlive() && !messageWriter.isAlive()){
                        cmd = Command.lastseenChannel(currentUsername,currentServerName, channelName);
                        transfer();
                    }
                }
                else if (action == 2) {
                    // creat a command to get the members
                    cmd = Command.getChannelMembers(currentUsername, currentServerName, channelName);
                    transfer();
                    if (!data.getKeyword().equals("channelMembers")){
                        inputHandler.printMsg("unable to receive data from server");
                        action = 0;
                    }
                    else {
                        ArrayList<String> members = (ArrayList<String>) data.getPrimary();
                        members.add("add new member to channel");

                        int option = inputHandler.showMenu(members);
                        if (option == members.size()) {
                            String newMember = inputHandler.receiveData("type the username that you want to add to channel");
                            cmd = Command.addOneMemberToChannel(currentUsername, newMember, currentServerName, channelName);
                            transfer();
                            if (data.getKeyword().equals("checkNewChannel") && (boolean) data.getPrimary()) {
                                inputHandler.printMsg("member successfully added to channel");
                            } else {
                                inputHandler.printMsg("something went wrong. try again later");
                            }
                        }
                    }
                }
                else if (action == 3){
                    cmd = Command.getPinnedMsgs(currentUsername, currentServerName, channelName);
                    transfer();
                    if (data.getKeyword().equals("pinnedMsgs")){
                        ArrayList<Message> pinnedMessages = (ArrayList<Message>) data.getPrimary();
                        inputHandler.printMsg("Pinned Messages :");
                        inputHandler.showMessages(pinnedMessages);
                    }

                }

            } while (action != 0);

        } while (choice != 0);
    }

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
            actions.add("press 0 to exit");

            action = inputHandler.showMenu(actions);
            if (action == 0)
                break;
            // create the command, for the keyword use the value in the abilities arrayList, to get the value use action - 1 index
            switch (actions.get(action - 1)) {
                case "create channel":
                    String newChannel = inputHandler.receiveData("enter channel name");
                    cmd = Command.newChannel(currentUsername, currentServerName, newChannel);
                    transfer();
                    if (data.getKeyword().equals("checkNewChannel") && !(boolean)data.getPrimary())
                        inputHandler.printMsg("oops! couldn't create channel. try again later.");
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
                case "see chat history ":
                    chatHistory(currentUsername);
                    break;
                case "pin message ":
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
            }

        } while (action != 0);

        return 0;
    }

    private Message chatHistory(String currentUsername) {
        String channel = inputHandler.receiveData("type a channel name to see chat history");

        cmd = Command.getChannelMsgs(currentUsername, currentServerName, channel, 10);
        transfer();
        if (!data.getKeyword().equals("")){
            inputHandler.printMsg("unable to receive data from server");
            return null;
        }
        ArrayList<Message> recentMessages = (ArrayList<Message>) data.getPrimary();

        StringBuilder stringBuilder = new StringBuilder();
        for (Message message : recentMessages) {
            stringBuilder.append(message).append("    ").append(message.getDateTime());
        }
        stringBuilder.append("press 0 to exit");
        int messageNum = inputHandler.showMenu(stringBuilder.toString(), recentMessages.size());
        if (messageNum == 0)
            return null;
        else
            return recentMessages.get(messageNum - 1);
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
