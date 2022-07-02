import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.nio.file.Files.readAllBytes;

public class FileUploader extends Thread {
    private ObjectOutputStream fout;
    private ArrayList<String> senderInfo;

    public FileUploader(ObjectOutputStream fout, ArrayList<String> senderInfo) {
        this.fout = fout;
        this.senderInfo = senderInfo;
    }

    @Override
    public void run() {
        FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);

        String fileNameAndType = dialog.getFile();
        String path = dialog.getDirectory()+"//"+dialog.getFile();
        System.out.println(dialog.getFile());
        byte[] bytes = new byte[0];
        try {
            bytes = readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileMessage fileMessage;
        try {
            if (senderInfo.size() == 1)
                fileMessage = new FileMessage(senderInfo.get(0), fileNameAndType);
            else
                fileMessage = new FileMessage(senderInfo.get(0), senderInfo.get(2), senderInfo.get(1), fileNameAndType);

            FileBytes fileBytes = FileBytes.toServer(fileMessage, bytes);
            fout.writeObject(fileBytes);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
