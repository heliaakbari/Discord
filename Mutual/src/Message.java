

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private LocalDateTime dateTime;
    private User sender;

    public Message(User sender) {
        this.sender = sender;
        dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public User getSender() {
        return sender;
    }
}
