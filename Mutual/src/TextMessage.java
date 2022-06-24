
public class TextMessage extends Message {
    private String text;

    public TextMessage(User sender, String text) {
        super(sender);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
