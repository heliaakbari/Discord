import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class InputHandler {

    public abstract int showMenu(String menu, int options);
    protected abstract int checkInput(int numberOfChoices) throws Exception;
    protected abstract String checkInput(String regex);
    public abstract void printMsg(String msg);
    public abstract String login();
    public abstract String signup();
    public abstract String usernameValidation();

}
