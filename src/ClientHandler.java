import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread{
    private Socket client;
    private ServerSide serverSide;
    private String threadID;

    public ClientHandler(Socket client, ServerSide serverSide) throws SocketException {
        this.client = client;
        this.serverSide = serverSide;
    }

    @Override
    public void run() {


    }
}
