import java.io.IOException;
import java.time.LocalDateTime;

public class TextMessage extends Message {
    private String text;

    public TextMessage(String sender, String text) {
        super(sender);
        this.text = text;
    }

    //from database to message
    public TextMessage(String sender, String text, LocalDateTime date) throws IOException {
        super(sender,date);
        this.text = text;
    }

    public TextMessage(String sender,String server,String channel,String text,LocalDateTime date) throws IOException {
        super(sender,channel,server,date);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return super.getSourceInfoAsString() + text;
    }
}
