import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * this class is a seperated thread for receiving messages in private chats or channels
 */
public class MessageReader extends Thread {

    private ObjectInputStream in;
    private Data data;
    private InputHandler inputHandler;
    // holds and array of received messages
    private ArrayList<Message> messageNumbering;
    private String currentUsername;

    /**
     * used to instantiate for channel messages
     *
     * @param in               object input stream of the socket
     * @param inputHandler     an object of InputHandler class
     * @param messageNumbering an array list of messages already sent  in the channel
     * @param currentUsername  this user's username
     */
    public MessageReader(ObjectInputStream in, InputHandler inputHandler, ArrayList<Message> messageNumbering, String currentUsername) {
        this.in = in;
        this.inputHandler = inputHandler;
        this.messageNumbering = messageNumbering;
        this.currentUsername = currentUsername;
    }

    /**
     * @param in           object input stream of the socket
     * @param inputHandler object input stream of the socket
     */
    public MessageReader(ObjectInputStream in, InputHandler inputHandler) {
        this.in = in;
        this.inputHandler = inputHandler;
        this.messageNumbering = new ArrayList<>();
    }

    @Override
    public void run() {
        Message message;
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            System.out.print(" ");
            try {
                data = (Data) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
            if (data.getKeyword().equals("exitChat")) {
                return;
            }

            message = (Message) data.getPrimary();
            messageNumbering.add(message);

            // pv messages are only shown with the sender's username
            if (data.getKeyword().equals("newPvMsg")) {
                stringBuilder.append(message.getSourceInfo().get(0)).append(" : ").append(message.getText());
            }
            // channel messages are shown with sender's username, channel and server
            // sender and receiver's username are checked not tobe the same so that messages aren't duplicated
            else if (data.getKeyword().equals("newChannelMsg") && !data.getUser().equals(currentUsername)) {
                stringBuilder.append(messageNumbering.size()).append(") ").append(message.getSourceInfo().get(0)).append(" : ").append(message.getText());
            }

            inputHandler.printMsg(stringBuilder.toString());
            stringBuilder = new StringBuilder("");

        }
    }
}
