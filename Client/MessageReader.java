import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * this class is a seperated thread for receiving messages in private chats or channels
 */
public class MessageReader extends Thread{

    private ObjectInputStream in;
    private Data data;
    private InputHandler inputHandler;
    // holds and array of received messages
    private ArrayList<Message> messageNumbering;
    private String currentUsername;

    // for channels
    public MessageReader(ObjectInputStream in, InputHandler inputHandler, ArrayList<Message> messageNumbering, String currentUsername){
        this.in = in;
        this.inputHandler = inputHandler;
        this.messageNumbering = messageNumbering;
        this.currentUsername = currentUsername;
    }

    // for private messages
    public MessageReader(ObjectInputStream in, InputHandler inputHandler){
        this.in = in;
        this.inputHandler = inputHandler;
        this.messageNumbering = new ArrayList<>();
    }

    @Override
    public void run() {
        Message message;
        StringBuilder stringBuilder = new StringBuilder();

        while (true){
            System.out.print(" ");
            try {
                data = (Data) in.readObject();
            } catch (IOException | ClassNotFoundException  e) {
                break;
            }

            if (data.getKeyword().equals("newPvMsg") || data.getKeyword().equals("newChannelMsg")){
                message = (Message) data.getPrimary();
                messageNumbering.add(message);
                if (message.getText().equals("0"))
                    break;
                // checks if the sender and receiver of the message aren't the same so that the message isn't duplicated in console
                if (!data.getUser().equals(currentUsername)) {
                    stringBuilder.append(message.getSourceInfo().get(0)).append(" : ").append(message.getText());
                    inputHandler.printMsg(stringBuilder.toString());
                    stringBuilder= new StringBuilder("");
                }
            }

        }
    }
}
