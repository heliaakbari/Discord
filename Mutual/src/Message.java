

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Message implements Serializable {

    private LocalDateTime dateTime;
    private String senderUsername;
    private HashMap<String, Integer> reactions;

    public Message(String sender) {
        this.senderUsername = sender;
        dateTime = LocalDateTime.now();
        reactions.put("like", 0);
        reactions.put("dislike", 0);
        reactions.put("laugh", 0);

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getSender() {
        return senderUsername;
    }
}
