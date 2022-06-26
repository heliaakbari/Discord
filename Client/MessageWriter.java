import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;


public class MessageWriter extends Thread{

    private ObjectOutputStream out;
    private Command cmd;
    private ArrayList<String> senderInfo;

    public MessageWriter(ObjectOutputStream out, ArrayList<String > senderInfo){
        this.out = out;
        this.senderInfo = senderInfo;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String text;
        Message message = null;

        while (true) {
            text = scanner.nextLine();
            if (text.equals("0")) {
                break;
            }
            else {
                if (senderInfo.size() == 1){
                    message = new TextMessage(senderInfo.get(0), text);
                } else{
                    message = new TextMessage(senderInfo.get(0), senderInfo.get(1), senderInfo.get(2), text);
                }

            }
            cmd = Command.newChannelMsg(senderInfo.get(0), message);
            try {
                out.writeObject(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
