import java.io.IOException;
import java.io.ObjectInputStream;

public class Listener extends Thread{
    private ClientHandler clientHandler;
    private ObjectInputStream in;


    public Listener(ClientHandler clientHandler,ObjectInputStream in){
        this.clientHandler = clientHandler;
        this.in = in;
    }
    @Override
    public void run() {
        Command cmd = null;
        while (true){
            cmd = null;
            try {
                cmd =(Command) in.readObject();
                System.out.println("got a command: "+cmd.getKeyword());
                clientHandler.getCommandFromListener(cmd);

                if(cmd.getKeyword().equals("exit")) {
                    Thread.currentThread().interrupt();
                }

            } catch (IOException e) {
                clientHandler.interrupt();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
