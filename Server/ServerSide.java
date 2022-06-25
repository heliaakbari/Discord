import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerSide {
    //private Inventory inventory;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clientHandlers;
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public ServerSide() {
        try {
            serverSocket = new ServerSocket(8642);
            clientHandlers = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(){

        while (true) {

            // making connection with client
            System.out.println("waiting for connection");
            try {
                client = serverSocket.accept();
                out = new ObjectOutputStream(client.getOutputStream());
                in  = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("new client connected successfully");

            // creating new thread for handling new connection
            ClientHandler clientHandler;
            try {
                clientHandler = new ClientHandler(client, this);
                clientHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (SocketException e) {
                System.out.println("client disconnected successfully");
            }
        }
    }


}
