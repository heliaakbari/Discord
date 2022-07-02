
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FileDownloader extends Thread{

    private ObjectInputStream fin;

    public FileDownloader( ObjectInputStream fin){
        this.fin = fin;
    }

    @Override
    public void run() {
        FileBytes fileBytes = null;
        try {
            fileBytes = (FileBytes) fin.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder path = new StringBuilder();

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){

            File file = jFileChooser.getSelectedFile();
            path.append(file.getAbsolutePath()).append(fileBytes.getFileName());
//            String filePath = "C:\\Users\\user\\Downloads\\discord";
            file = new File(path.toString());

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(fileBytes.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
