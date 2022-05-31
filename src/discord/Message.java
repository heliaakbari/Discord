package discord;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String text;
    private LocalDateTime dateTime;
    private User sender;

    public Message(String text, User sender) {
        this.text = text;
        this.sender = sender;
        dateTime = LocalDateTime.now();
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public User getSender() {
        return sender;
    }
}
