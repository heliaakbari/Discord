import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread{
    private Socket client;
    private ServerSide serverSide;
    private String threadID;

    public ClientHandler(Socket client, ServerSide serverSide, String id) throws SocketException {
        this.client = client;
        this.serverSide = serverSide;
        this.threadID = id;
    }

    @Override
    public void run() {


    }
}
