package discord;

public enum Relationship {
    Friend,
    Block,
    Friend_pending;

    private User sender;
    private User receiver;

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
