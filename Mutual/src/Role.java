package discord;

import java.util.ArrayList;
import java.util.Arrays;

public class Role {

    private String values;
    private final ArrayList<String> abilities = new ArrayList<>(Arrays.asList("creat channel", "remove channel", "remove member",
            "restrict member", "ban member", "change server name", "see chat history", "pin message", "delete server"));

    public Role(String values) {
        this.values = values;
    }

    @Override
    public String toString() {
        int choiceNumber = 1;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++){
            if (values.charAt(i) == 1){
                stringBuilder.append(choiceNumber + " : " + abilities.get(i) + "\n");
                choiceNumber++;
            }
        }

        return stringBuilder.toString();
    }
}
