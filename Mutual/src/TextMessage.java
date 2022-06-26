
public class TextMessage extends Message {
    private String text;

    public TextMessage(String sender, String text) {
        super(sender);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return super.getSourceInfo() + text;
    }
}
