package discord;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileMessage extends Message{

    private byte[] fileBytes;
    private File file;

    public FileMessage(User sender, File file) throws IOException {
        super(sender);
        this.file = file;
        fileBytes = Files.readAllBytes(file.toPath());
    }
}
