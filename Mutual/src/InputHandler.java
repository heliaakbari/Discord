import java.util.ArrayList;

public abstract class InputHandler {

    public abstract int showMenu(String menu, int options);
    public abstract int showMenu(ArrayList<String> menu);

    protected abstract int checkInput(int numberOfChoices) throws Exception;
    protected abstract String checkInput(String regex, int minLength, int maxLength) throws Exception;

    public abstract void printMsg(String msg);
    public abstract void printError(Exception e);

    public abstract ArrayList<String> login();
    public abstract User signup();
    public abstract String usernameValidation();

    public abstract String friendRequest();
    public abstract boolean showFriendInfo(User friend);
    public abstract int showRequest(User user);


}
