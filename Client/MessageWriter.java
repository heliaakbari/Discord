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
    private ArrayList<Message> messageNumbering;

    // for channels
    public MessageWriter(ObjectOutputStream out, ArrayList<String > senderInfo, ArrayList<Message> messageNumbering){
        this.out = out;
        this.senderInfo = senderInfo;
        this.messageNumbering = messageNumbering;
    }

    // for private messages
    public MessageWriter(ObjectOutputStream out, String senderInfo, String receiverInfo){
        this.out = out;
        this.senderInfo = new ArrayList<>(List.of(senderInfo));
        this.receiverInfo = receiverInfo;
        this.messageNumbering = new ArrayList<>();
    }

    @Override
    public void run() {
        if (senderInfo.size() == 1)
            pvChat();
        else
            channelChat();
        Thread.currentThread().interrupt();
    }

    private void channelChat(){
        Message message;
        Scanner scanner = new Scanner(System.in);
        String text;

        while (true){
            text = scanner.nextLine();
            if (text.equals("0")) {
                Command cmd = Command.lastseenChannel(senderInfo.get(0),senderInfo.get(2),senderInfo.get(1));
                try {
                    out.writeObject(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            else if (text.contains("react ")){
                String[] splitted = text.split(" ");
                message = messageNumbering.get(Integer.parseInt(splitted[1]) - 1);
                System.out.println(splitted[2].equals("like"));
                cmd = Command.newReaction(senderInfo.get(0), message, splitted[2]);
            }
            else {
                message = new TextMessage(senderInfo, text);
                cmd = Command.newChannelMsg(senderInfo.get(0),senderInfo.get(2),senderInfo.get(1), message);
            }
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
            if (text.equals("0")) {
                Command cmd = Command.lastseenPv(senderInfo.get(0),receiverInfo);

                try {
                    out.writeObject(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            message = new TextMessage(senderInfo, text);
            cmd = Command.newPvMsg(senderInfo.get(0), receiverInfo, message);
            try {
                out.writeObject(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
