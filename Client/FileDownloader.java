import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileDownloader extends Thread{

    private String fileName;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public FileDownloader(String fileName, ObjectOutputStream out, ObjectInputStream in){
        this.fileName = fileName;
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        // create a command to ask for the file with given name
        Command cmd = null;
        Data data = null;

        try {
            out.writeObject(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] file = (byte[]) data.getPrimary();


    }
}
