import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

public class MessageReader extends Thread{

    private ObjectInputStream in;
    private Data data;
    private InputHandler inputHandler;

    public MessageReader(ObjectInputStream in, InputHandler inputHandler){
        this.in = in;
        this.inputHandler = inputHandler;
    }

    @Override
    public void run() {

        while (true){
            try {
                data = (Data) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            inputHandler.printMsg((String) data.getPrimary());
        }

    }
}
