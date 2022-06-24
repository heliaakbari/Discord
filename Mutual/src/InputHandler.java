import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public abstract class InputHandler {

    public abstract int showMenu(String menu, int options);
    public abstract int showMenu(ArrayList<String> menu, int options);
    protected abstract int checkInput(int numberOfChoices) throws Exception;
    protected abstract String checkInput(String regex);
    public abstract void printMsg(String msg);
    public abstract ArrayList<String> login();
    public abstract ArrayList<String> signup();
    public abstract String usernameValidation();

}
