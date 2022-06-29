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
        try {
            listener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getCommandFromListener(Command cmd){
        Data dt = serverSide.moveCmd(cmd,this);
        if(cmd.getKeyword().equals("exit")){
            return;
        }
        if(!cmd.getKeyword().equals("newPvMsg") && !cmd.getKeyword().equals("newChannelMsg")) {
            try {
                out.writeObject(dt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ;
    }

    public void sendInstantMessage(Data dt){
        try {
            out.writeObject(dt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
