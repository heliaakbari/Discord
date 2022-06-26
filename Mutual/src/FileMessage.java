
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileMessage extends Message{

    private String fileName;
    private String format;
    private byte[] fileBytes;
    private File file;

    public FileMessage(String sender, File file, String fileName, String format) throws IOException {
        super(sender);
        this.file = file;
        fileBytes = Files.readAllBytes(file.toPath());
        this.fileName = fileName;
        this.format = format;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFormat() {
        return format;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return super.getSourceInfoAsString() + " a file";
    }
}
