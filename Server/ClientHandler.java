import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread{
    private Socket client;
    private ServerSide serverSide;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket client, ObjectInputStream in, ObjectOutputStream out, ServerSide serverSide) throws SocketException {
        this.client = client;
        this.in = in;
        this.out = out;
        this.serverSide = serverSide;
    }

    @Override
    public void run() {
        Listener listener = new Listener(this,in);
        listener.start();
    }

    public Data getCommandFromListener(Command cmd){
        Data dt = serverSide.moveCmd(cmd,this);
        try {
            out.writeObject(dt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public void sendInstantMessage(Data dt){
        try {
            out.writeObject(dt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
