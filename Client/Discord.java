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

    public void createServer(){}

    public void enterDirectMessages(){}

    public void enterRelationshipsList(){
        int choice;
        do {
            choice = inputHandler.showMenu("""
                    1) friends list
                    2) blocked list
                    3) pending list
                    4) send friend request
                    5) block someone
                    press 0 to exit""", 5);

            // create appropriate command based on each option
            switch (choice) {
                case 1 -> ;
                case 2 -> ;
                case 3 -> ;
                case 4 -> ;
                case 5 -> ;
            }
        } while (choice != 0);
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
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("c:\\")));
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        try {
                            byte[] photo = Files.readAllBytes(selectedFile.toPath());
                            String fileType = fileChooser.getTypeDescription(selectedFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // create a command to set profile photo
                    break;
                case 2 :
                    while (true) {
                        String newUsername = inputHandler.usernameValidation();
                        if (newUsername.equals("0"))
                            break;
                        // create a command to check if it exists if not found the condition is true
                        if (condition){
                            // create a command to set the new username
                            break;
                        }
                    }
                    break;
            }

        } while (choice != 0);
    }

}
