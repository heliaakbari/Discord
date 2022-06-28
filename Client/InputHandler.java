import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class InputHandler  implements Serializable {

    public abstract int showMenu(String menu, int options);
    public abstract int showMenu(ArrayList<String> menu);
    public abstract void showMessages(ArrayList<Message> messages);

    protected abstract int checkInput(int numberOfChoices) throws Exception;
    protected abstract String checkInput(String regex, int minLength, int maxLength) throws Exception;

    public abstract void printMsg(String msg);
    public abstract void printError(Exception e);

    public abstract ArrayList<String> login();
    public abstract User signup();
    public abstract String usernameValidation();

    public abstract String receiveData(String question);
    public abstract int receiveData(User user, String optionsList, int numberOfOptions);

    public abstract ArrayList<ArrayList<String>> createServer();
    public abstract HashMap<String, Role> defineRoles();


}
