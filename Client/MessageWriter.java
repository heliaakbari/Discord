import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * this class is a seperated thread for sending messages in private chats or channels
 * it will transfer messages to server side
 */
public class MessageWriter extends Thread{

    private ObjectOutputStream out;
    private ObjectOutputStream fout;
    private ObjectInputStream fin;
    private Command cmd;
    private ArrayList<String> senderInfo;
    private String receiverInfo;
    private ArrayList<Message> messageNumbering;

    /**
     * used to instantiate for channels messages
     * @param out object output stream of the socket
     * @param fout  object output stream of the socket on the second port only used to transfer files
     * @param senderInfo an array list of sender's info including their username and name of the channel and server where they're sending the message
     * @param messageNumbering an arraylist of already existing messages in the chat
     */
    public MessageWriter(ObjectOutputStream out, ObjectOutputStream fout, ObjectInputStream fin, ArrayList<String > senderInfo, ArrayList<Message> messageNumbering){
        this.out = out;
        this.senderInfo = senderInfo;
        this.messageNumbering = messageNumbering;
        this.fout = fout;
        this.fin = fin;
    }

    /**
     * used to instantiate for pv messages
     * @param out object output stream of the socket
     * @param fout  object output stream of the socket on the second port only used to transfer files
     * @param senderInfo an array list of sender info with only one element : sender's username
     * @param receiverInfo receiver's username
     */
    public MessageWriter(ObjectOutputStream out, ObjectOutputStream fout,ObjectInputStream fin,  String senderInfo, String receiverInfo){
        this.out = out;
        this.senderInfo = new ArrayList<>(List.of(senderInfo));
        this.receiverInfo = receiverInfo;
        this.messageNumbering = new ArrayList<>();
        this.fout = fout;
        this.fin = fin;
    }

    @Override
    public void run() {
        if (senderInfo.size() == 1)
            pvChat();
        else
            channelChat();
        Thread.currentThread().interrupt();
    }

    /**
     * used to receive messages on channels
     */
    private void channelChat(){
        Message message;
        Scanner scanner = new Scanner(System.in);
        String text;

        while (true){
            text = scanner.nextLine();
            // if user decides to quit the chat
            if (text.equals("0")) {
                Command cmd = Command.lastseenChannel(senderInfo.get(0),senderInfo.get(2),senderInfo.get(1));
                try {
                    out.writeObject(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            // for sending files
            else if (text.equals("send file")){
                sendFile();
            }
            else if (text.contains("download file")){
                downloadFile(text);

            }
            // for reaction
            else if (text.contains("react ")){
                String[] splitted = text.split(" ");
                message = messageNumbering.get(Integer.parseInt(splitted[1]) - 1);
                cmd = Command.newReaction(senderInfo.get(0), message, splitted[2]);

                try {
                    out.writeObject(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // for text messages
            else {
                message = new TextMessage(senderInfo, text);
                cmd = Command.newChannelMsg(senderInfo.get(0),senderInfo.get(2),senderInfo.get(1), message);

                try {
                    out.writeObject(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * used to receive messages in private chats
     */
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
            else if (text.equals("send file")){
                sendFile();
            }
            else if (text.contains("download file")){
                downloadFile(text);

            }
            else {
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

    /**
     * sends a download command to server then starts another thread to receive the file on another port
     * @param text
     */
    private void downloadFile(String text) {
        String[] splitted = text.split(" ");
        StringBuilder fileName = new StringBuilder();
        for (int i = 2; i <splitted.length ; i++) {
            fileName.append(splitted[i]);
        }
        try{
            if (senderInfo.size() == 1){
                cmd = Command.download(senderInfo.get(0),receiverInfo, fileName.toString(), false);
            }
            else {
                cmd = Command.download(senderInfo.get(2), senderInfo.get(1), fileName.toString(), true);
            }
            out.writeObject(cmd);
        } catch (IOException e){
            e.printStackTrace();
        }

        FileDownloader fileDownloader = new FileDownloader(fin);
        fileDownloader.start();
    }

    /**
     * sends an upload command to server then starts a new thread for uploading it on another port
     */
    private void sendFile() {
        if (senderInfo.size() == 1)
            cmd = Command.upload(receiverInfo, null ,null, false);
        else
            cmd = Command.upload(null, senderInfo.get(2), senderInfo.get(1), true);
        try {
            out.writeObject(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileUploader fileUploader = new FileUploader(fout, senderInfo);
        fileUploader.start();
    }


}
