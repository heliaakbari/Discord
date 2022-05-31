package discord;
import java.io.Serializable;

public class FriendRequest implements Serializable {
    private User sender;
    private User receiver;
    private String status;

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        status = "pending";
    }

    public void reject(){
        status = "rejected";
    }

    public void accept(){
        status = "accepted";
    }
}
