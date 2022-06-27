import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MessageWriter extends Thread{

    private ObjectOutputStream out;
    private Command cmd;
    private ArrayList<String> senderInfo;
    private String receiverInfo;

    public MessageWriter(ObjectOutputStream out, ArrayList<String > senderInfo){
        this.out = out;
        this.senderInfo = senderInfo;
    }

    public MessageWriter(ObjectOutputStream out, String senderInfo, String receiverInfo){
        this.out = out;
        this.senderInfo = new ArrayList<>(List.of(senderInfo));
        this.receiverInfo = receiverInfo;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String text;
        Message message = null;

        if (senderInfo.size() == 1)
            pvChat();
        else
            channelChat();
    }

    private void channelChat(){
        Message message;
        Scanner scanner = new Scanner(System.in);
        String text;

        while (true){
            text = scanner.nextLine();
            if (text.equals("0"))
                break;
            else
                message = new TextMessage(senderInfo, text);

            cmd = Command.newChannelMsg(senderInfo.get(0), message);
            try {
                out.writeObject(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pvChat(){
        Message message;
        Scanner scanner = new Scanner(System.in);
        String text;

        while(true){
            text = scanner.nextLine();
            if (text.equals("0"))
                break;
            message = new TextMessage(senderInfo, text);
            cmd = Command.newPvMsg(senderInfo.get(0), receiverInfo, message );
            try {
                out.writeObject(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
