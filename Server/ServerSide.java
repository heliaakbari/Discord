import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerSide {

    private ServerSocket serverSocket;
    private HashMap<String,ClientHandler> clientHandlers;
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DatabaseManager dbm = new DatabaseManager();
    //first one is the person and the other is their friend
    private HashMap<String,String> activePvs= new HashMap<>();
    //first one is the user and the other is array [server,channel] form
    private HashMap<String,ArrayList<String>> activeChannels= new HashMap<>();

    public ServerSide() {
        try {
            serverSocket = new ServerSocket(8643);
            clientHandlers = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        dbm.start();

        while (true) {

            // making connection with client
            System.out.println("waiting for connection");
            try {
                client = serverSocket.accept();
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("new client connected successfully");

            // creating new thread for handling new connection
            ClientHandler clientHandler;
            try {
                clientHandler = new ClientHandler(client, in, out, this);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (SocketException e) {
                System.out.println("client disconnected successfully");
            }
        }
    }

    public void addClientHandler (String username,ClientHandler clientHandler ){
        clientHandlers.put(username,clientHandler);
    }

    public Data moveCmd(Command cmd,ClientHandler clientHandler){
        Data dt = dbm.cmdManager.process(cmd);
        switch (dt.getKeyword()){
            case "checkSignUp" :
            case "checkLogin" :
                if (((boolean)dt.getPrimary())){
                    addClientHandler(dt.getUser(),clientHandler);
                }
            break;
            case "checkChangeUsername":
                if (((boolean)dt.getPrimary())){
                   if (clientHandlers.containsKey(dt.getUser())){
                       ClientHandler ch = clientHandlers.get(dt.getUser());
                       clientHandlers.remove(dt.getUser());
                       clientHandlers.put((String) dt.getSecondary(),ch);
                   }

                   if(activePvs.containsKey(dt.getUser())){
                       String theOther = activePvs.get(dt.getUser());
                       activePvs.remove(dt.getUser());
                       activePvs.put((String) dt.getSecondary(),theOther);
                   }

                   else if(activeChannels.containsKey(dt.getUser())){
                       ArrayList<String> place = activeChannels.get(dt.getUser());
                       activeChannels.remove(dt.getUser());
                       activeChannels.put((String)dt.getSecondary(),place);
                   }
                }
            break;
            case "checkChangeServerName":
                for (HashMap.Entry<String, ArrayList<String>> set :
                        activeChannels.entrySet()) {
                    if(set.getValue().get(0).equals(dt.getServer())){
                        ArrayList<String> newPlace = new ArrayList<>();
                        newPlace.add((String) dt.getSecondary());
                        newPlace.add(set.getValue().get(1));
                        activeChannels.replace(set.getKey(),newPlace);
                    }
                }
            break;

            case "checkDeleteChannel":
                if(((boolean) dt.getPrimary())==true){
                    for (HashMap.Entry<String, ArrayList<String>> set :
                            activeChannels.entrySet()) {
                        if(set.getValue().get(0).equals(dt.getServer()) && set.getValue().get(1).equals(dt.getChannel())){
                            activeChannels.remove(set);
                        }
                    }
                }
            break;

            case "checkDeleteServer":
                if(((boolean) dt.getPrimary())==true){
                    for (HashMap.Entry<String, ArrayList<String>> set :
                            activeChannels.entrySet()) {
                        if(set.getValue().get(0).equals(dt.getServer()) ){
                            activeChannels.remove(set);
                        }
                    }
                }
            break;
        }

        switch (cmd.getKeyword()){
            case "lastseenPv":
                activePvs.remove(cmd.getUser());
            break;

            case "lastseenChannel":
                activeChannels.remove(cmd.getUser());
            break;

            case "tellPv":
                activePvs.put(cmd.getUser(),(String)cmd.getPrimary());
            break;

            case "tellChannel":
                ArrayList<String> place = new ArrayList<>();
                place.add(cmd.getServer());
                place.add(cmd.getChannel());
                activeChannels.put(cmd.getUser(),place);
            break;

            case "newPvMsg" :
                instantPvMsg(cmd);
            break;

            case "newChannelMsg" :
                instantChannelMsg(cmd);
            break;
        }
        return dt;
    }

    public void instantPvMsg(Command cmd){
        if(cmd.getKeyword().equals("newPvMsg")){
            printClientHandlers();
            String receiver = (String) cmd.getSecondary();
            if(activePvs.containsKey(receiver)){
               String sender = activePvs.get(receiver);
               if(sender.equals(cmd.getUser())){
                   Data dt = Data.newPvMsg(receiver,(Message) cmd.getPrimary());
                   clientHandlers.get(receiver).sendInstantMessage(dt);
               }
            }
        }
    }

    public void instantChannelMsg(Command cmd){
        if(cmd.getKeyword().equals("newChannelMsg")) {
            printClientHandlers();
           String place = cmd.getServer()+"/"+cmd.getChannel();
           ArrayList<String> onlinePeople = new ArrayList<>();
            for (HashMap.Entry<String, ArrayList<String>> set :
                    activeChannels.entrySet()) {
                if(set.getValue().get(0).equals(cmd.getServer()) && set.getValue().get(1).equals(cmd.getChannel() )){
                    onlinePeople.add(set.getKey());
                }
            }
            Data dt = Data.newChannelMsg(cmd.getServer(),cmd.getChannel(),(Message)cmd.getPrimary());
            for(String person : onlinePeople){
                clientHandlers.get(person).sendInstantMessage(dt);
            }
        }
    }

    public void printClientHandlers(){
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        for (HashMap.Entry<String, ClientHandler> set :
                clientHandlers.entrySet()) {

            // Printing all elements of a Map
            System.out.println(set.getKey());
        }
        System.out.println("!!!!!!!!!!!!!!!!!!");
    }
}
