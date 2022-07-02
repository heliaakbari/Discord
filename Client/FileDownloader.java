
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
        System.out.println("thread started");
        FileBytes fileBytes = null;
        try {
            fileBytes = (FileBytes) fin.readObject();
            System.out.println("got the file fro server");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        StringBuilder path = new StringBuilder();
//        JFileChooser jFileChooser = new JFileChooser();
//        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
//
//            File file = jFileChooser.getSelectedFile();
//            path.append(file.getAbsolutePath()).append(fileBytes.getFileName());
//
//            file = new File(path.toString());
//
//            try {
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(fileBytes.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        String filePath = "C:\\discord";
        filePath = filePath + "\\" +fileBytes.getFileName();
        File file = new File(filePath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileBytes.getBytes());
            System.out.println("file saved!");
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
