

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private LocalDateTime dateTime;
    private String senderUsername;

    public Message(String sender) {
        this.senderUsername = sender;
        dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getSender() {
        return senderUsername;
    }
}
