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

    protected String checkInput (String regex){
        String input = scanner.nextLine();
        if (!input.matches(regex) && !input.equals("0"))
            return null;
        else
            return input;

    }

    public void printMsg(String msg){
        System.out.println(msg);
    }

    public int showMenu(String menu, int options){

        System.out.println(menu);
        boolean successful = false;
        int choice = 0;
        while (!successful){
            try {
               choice = checkInput(options);
               successful = true;
            } catch (Exception e){
                System.err.println("you must choose one of the numbers in the menu");
            }
        }
        return choice;
    }

//    public int showEntranceMenu(){
//        System.out.println("welcome");
//        System.out.println("""
//                1) signup
//                2) login
//                press 0 to exit
//                """);
//
//        boolean successful = false;
//        int choice = 0;
//        while (!successful) {
//            try {
//                choice = checkInput(2);
//                successful = true;
//            } catch (Exception e) {
//                System.err.println("you must choose one of the numbers in the menu");
//            }
//        }
//        return choice;
//    }

//    public int showDiscordMenu(){
//
//        System.out.println("""
//                1) servers list
//                2) direct messages
//                3) friends list
//                4) blocked list
//                5) pending list""");
//
//
//        boolean successful = false;
//        int choice = 0;
//        while (!successful){
//            try {
//                choice = checkInput(5);
//                successful = true;
//            } catch (Exception e){
//                System.err.println("you must choose one of the numbers in the menu");
//            }
//        }
//        return choice;
//    }

    public String login(){

        StringBuilder stringBuilder = new StringBuilder("");
        System.out.println("press 0 to exit");
        System.out.print("username -> ");

        input = scanner.nextLine();
        if (input.equals("0"))
            return "0";
        stringBuilder.append(" ").append(input);

        System.out.println("press 0 to exit");
        System.out.print("password -> ");
        input = scanner.nextLine();
        if (input.equals("0"))
            return "0";
        stringBuilder.append(" ").append(input);
        return stringBuilder.toString();
    }

    public String signup() {
        System.out.println("password must be at least 8 characters and must contain capital and small english alphabets and numbers");
        System.out.println("press 0 to exit");
        System.out.print("password -> ");

        String password = checkInput("");
        // یه کاری کن به حای ریترن استیت منت ترد داشته باشی
        while (password == null) {
            System.out.println("invalid password format. try again!");
            password = checkInput("");
        }


        System.out.println("press 0 to exit");
        System.out.print("email address -> ");

        String email = checkInput("");
        while (email == null) {
            System.out.println("invalid email format. try again!");
            email = checkInput("");
        }
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(" ").append(password).append(" ").append(email);


        System.out.println("would yu like to add a phone number as well?");
        System.out.println("1) yes 2) no");
        int option = scanner.nextInt();
        if (option == 1) {
            System.out.println("press 0 to exit");
            System.out.print(" phone number -> ");

            String phoneNum = checkInput("");
            while (phoneNum == null) {
                System.out.println("invalid phone number format. try again!");
                phoneNum = checkInput("");
            }

            stringBuilder.append(" ").append(phoneNum);
        }

        return stringBuilder.toString();
    }

    public String usernameValidation(){
        System.out.println("username must be at least 6 characters and only containing english alphabet and numbers");
        System.out.println("press 0 to exit");
        System.out.print("username -> ");

        input = checkInput("");
        while (input == null){
            System.out.println("invalid username format. try again!");
            input = checkInput("");
        }
        return input;
    }

}
