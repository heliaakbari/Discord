import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Console extends InputHandler{

    private String input;
    private final Scanner scanner = new Scanner(System.in);

    protected int checkInput(int numberOfChoices) throws Exception {
        int choice;
        choice = scanner.nextInt();
        if (choice == 0)
            return 0;
        if (choice < 1 || choice > numberOfChoices)
            throw new Exception("input out of range");
        return choice;
    }

    protected String checkInput(String regex, int minLength, int maxLength) throws Exception {
        String input = scanner.nextLine();
        if (!input.matches(regex))
            throw new Exception("input doesn't match the specified format");
        else if (input.length() < minLength)
            throw new Exception("input must be at least " + minLength + "characters");
        else if (input.length() > maxLength)
            throw new Exception("input must be at most " + maxLength + "characters");
        else
            return input;
    }

    public void printMsg(String msg){
        System.out.println(msg);
    }

    public int showMenu(String menu, int options){

        System.out.println(menu);
        return getUserOption(options);
    }

    public int showMenu(ArrayList<String> menu){
        int optionNumber = 1;
        for (String option: menu) {
            System.out.println(optionNumber + option);
            optionNumber++;
        }
        return getUserOption(optionNumber - 1);
    }

    private int getUserOption(int options) {
        boolean successful = false;
        int choice = 0;
        while (!successful){
            try {
               choice = checkInput(options);
               successful = true;
            } catch (Exception e){
                System.err.println( e.getMessage() + "you must choose one of the numbers in the menu");
            }
        }
        return choice;
    }

    public ArrayList<String> login(){

        ArrayList<String> info = new ArrayList<>();

        System.out.print("username -> ");
        input = scanner.nextLine();
        if (input.equals("0"))
            return null;
        info.add(input);

        System.out.print("password -> ");
        input = scanner.nextLine();
        if (input.equals("0"))
            return null;
        info.add(input);
        return info;
    }

    public User signup() {

        User user = new User();

        System.out.println("password must contain capital and small english alphabets and numbers");
        System.out.println("press 0 to exit");
        System.out.print("password -> ");
        boolean successful = false;

        while (!successful){
            try {
                input = checkInput("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])",8, 20);
                if (input.equals("0"))
                    return null;
                user.setPassword(input);
                successful = true;
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }

        successful = false;
        System.out.println("press 0 to exit");
        System.out.print("email address -> ");

        while (!successful){
            try {
                input = checkInput("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",11, 20);
                if (input.equals("0"))
                    return null;
                user.setEmail(input);
                successful = true;
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }

        System.out.println("would you like to add a phone number as well?");
        System.out.println("1) yes 2) no");
        int option = scanner.nextInt();
        if (option == 1) {
            successful = false;
            System.out.println("press 0 to exit");
            System.out.print(" phone number -> ");

            while (!successful){
                try {
                    input = checkInput("^[0-9]+$",11, 15);
                    if (input.equals("0"))
                        return null;
                    user.setPhoneNum(input);
                    successful = true;
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        }

        System.out.println("would you like to define a status?");
        System.out.println("1) yes    2) no");
        option = scanner.nextInt();
        if (option == 1){
            ArrayList<String> status = new ArrayList<>(Arrays.asList("1) online", "2) idle", "3) do not disturb", "4) invisible", "press 0 to exit"));
            int choice = showMenu(status);
            if (choice != 0)
                user.setStatus(status.get(choice - 1));

        }

        System.out.println("you can also add a profile photo in account settings");
        return user;
    }

    public String usernameValidation(){
        System.out.println("username must be at least 6 characters and only containing english alphabet and numbers");
        System.out.println("press 0 to exit");
        System.out.print("username -> ");

        boolean successful = false;
        while (!successful){
            try {
                input = checkInput("^[0-9a-zA-Z]+$",6 , 20);
                successful = true;
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }

        return input;
    }

    public void printError(Exception e){
        e.printStackTrace();
    }

    public String receiveData(String question){
        System.out.println(question);
        return scanner.nextLine();
    }

    public boolean showFriendInfo(User friend){
        System.out.println(friend);
        int choice = showMenu("0) back\n1) block this person", 1);
        return (choice == 1);
    }

    public int showRequest(User user){
        System.out.println(user.getUsername() + " wants to follow you");
        System.out.println(user);
        return showMenu("1) accept    2) reject\npress 0 to exit", 2);
    }

    public ArrayList<ArrayList<String>> createServer(){
        ArrayList<ArrayList<String>> serverInfo = new ArrayList<>();
        ArrayList<String> eachInfo = new ArrayList<>();
        System.out.println("enter the name of the server");
        eachInfo.add(scanner.nextLine());
        serverInfo.add(eachInfo);

        eachInfo = new ArrayList<>();
        String input;
        System.out.println("ok now let's create channels, type the name of the channels, press 0 when finished");
        while (true){
            input = scanner.nextLine();
            if (input.equals("0"))
                break;
            else
                eachInfo.add(input);
        }
        serverInfo.add(eachInfo);


        eachInfo = new ArrayList<>();
        System.out.println("now type the username of the people you want to add to server, press 0 when finished");
        while (true){
            input = scanner.nextLine();
            if (input.equals("0"))
                break;
            else
                eachInfo.add(input);
        }
        serverInfo.add(eachInfo);

        return serverInfo;

    }

    public HashMap<String, Role> defineRoles() {
        System.out.println("you can define special roles in server. to do so, you must first define the role and then give the corresponding username");
        Role role;
        String roleName, username;
        StringBuilder values = new StringBuilder();
        int choice;
        HashMap<String, Role> roles = new HashMap<>();

        while (true){
            System.out.println("enter the role name");
            roleName = scanner.nextLine();
            for (String ability : Role.abilities){
                System.out.println(ability + " ?");
                System.out.println("1) yes    2) no");
                choice = scanner.nextInt();
                values.append((choice == 1) ? "1" : "0");
            }
            role = new Role(values.toString(), roleName);
            System.out.println("type the username for this role");
            scanner.nextLine();
            username = scanner.nextLine();
            roles.put(username, role);


            System.out.println("wanna add another role?\n1) yes    2) no");
            choice = scanner.nextInt();
            if (choice == 2)
                break;
        }
        return roles;
    }
}
