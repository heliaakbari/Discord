public class FileBytes {
    private byte[] bytes;
    private String fileName;
    private FileMessage fileMessage;
    public void setBytes(byte[] bytes,String filename) {
        this.bytes = bytes;
        this.fileName = filename;
    }

    private FileBytes(){

    }

    public static FileBytes toServer(FileMessage fileMessage,byte[] bytes){
        FileBytes fb = new FileBytes();
        fb.fileMessage = fileMessage;
        return fb;
    }

    public FileMessage getFileMessage() {
        return fileMessage;
    }

    public static FileBytes toClient(String fileName, byte[] bytes){
        FileBytes fb = new FileBytes();
        fb.bytes = bytes;
        fb.fileName = fileName;
        return fb;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
