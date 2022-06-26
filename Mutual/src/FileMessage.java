
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

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

    public FileMessage(String sender,String server,String channel,File file, String fileName, String format) throws IOException {
        super(sender,channel,server);
        this.file = file;
        fileBytes = Files.readAllBytes(file.toPath());
        this.fileName = fileName;
        this.format = format;
    }

        //from database to message
    public FileMessage(String sender,  LocalDateTime date, String fileName, byte[] fileBytes, String format) throws IOException {
        super(sender,date);
        this.fileBytes = fileBytes;
        this.fileName = fileName;
        this.format = format;
    }

    public FileMessage(String sender,String server,String channel,LocalDateTime date, String fileName,byte[] fileBytes, String format) throws IOException {
        super(sender,channel,server,date);
        this.fileBytes = fileBytes;
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
