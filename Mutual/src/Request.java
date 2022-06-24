
import java.io.Serializable;

public class Request implements Serializable {
    private String sender;
    private String receiver;
    private Relationship relationship;

    public Request(String sender, String receiver, String relationship) {
        this.sender = sender;
        this.receiver = receiver;
        this.relationship = Relationship.valueOf(relationship);
    }

    public void reject(){
         relationship = null;
    }

    public void accept(){
        relationship = Relationship.Friend;
    }
}
