

public enum Relationship {
    Friend,
    Block,
    Friend_pending,
    Rejected;

    private String sender;
    private String receiver;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

}
