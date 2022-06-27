import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MessageReader extends Thread{

    private ObjectInputStream in;
    private Data data;
    private InputHandler inputHandler;
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
    }

    @Override
    public void run() {
        Message message = null;
        StringBuilder stringBuilder = new StringBuilder();

        while (true){
            try {
                data = (Data) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (data.getKeyword().equals("newPvMsg") || data.getKeyword().equals("newChannelMsg")){
                message = (Message) data.getPrimary();
                messageNumbering.add(message);
                if (message.getText().equals("0"))
                    break;
                if (!data.getUser().equals(currentUsername)) {
                    stringBuilder.append(message.getSourceInfo().get(0)).append(" : ").append(message.getText()).append("\n");
                    inputHandler.printMsg(stringBuilder.toString());
                }
            }

        }
    }
}
