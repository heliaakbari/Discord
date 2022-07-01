import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

import static java.nio.file.Files.readAllBytes;

public class ClientHandler extends Thread{
    private Socket client;
    private ServerSide serverSide;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ObjectInputStream fin;
    private ObjectOutputStream fout;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");

    public ClientHandler(Socket client, ObjectInputStream in, ObjectOutputStream out, ObjectInputStream fin,ObjectOutputStream fout,ServerSide serverSide) throws SocketException {
        this.client = client;
        this.in = in;
        this.out = out;
        this.fin = fin;
        this.fout = fout;
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
        if(!cmd.getKeyword().equals("download") && !cmd.getKeyword().equals("upload") && !cmd.getKeyword().equals("newPvMsg") && !cmd.getKeyword().equals("newChannelMsg")) {
            try {
                out.writeObject(dt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (cmd.getKeyword().equals("upload")){
            FileBytes fb = null;
            try {
                try {
                    fb = (FileBytes) fin.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            File outputFile = new File("C:\\DiscordFiles\\"+fb.getFileMessage().getSourceInfo().get(0)+fb.getFileMessage().getDateTime().format(dateTimeFormatter)+fb.getFileName());
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                outputStream.write(fb.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (cmd.getKeyword().equals("download")){
            byte[] bytes = null;
            FileMessage fileMessage =(FileMessage) cmd.getPrimary();
            try {
                bytes = readAllBytes(Paths.get("C:\\DiscordFiles\\"+fileMessage.getSourceInfo().get(0)+fileMessage.getDateTime().format(dateTimeFormatter)+fileMessage.getFileName()));
            }
            catch (NoSuchFileException e){
                System.out.println("the file with path doesnt exists");
            }
            catch (IOException e){
                e.printStackTrace();
            }
            try {
                fout.writeObject(FileBytes.toClient(fileMessage.getFileName(),bytes));
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
