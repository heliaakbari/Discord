
import java.util.ArrayList;
import java.util.Arrays;

public class Role {

    private String values;
    private final ArrayList<String> abilities = new ArrayList<>(Arrays.asList("creat channel", "remove channel", "remove member",
            "restrict member", "ban member", "change server name", "see chat history", "pin message", "delete server"));

    public Role(String values) {
        this.values = values;
    }


    public ArrayList<String> getAbilities() {
        ArrayList<String> availableAbilities = new ArrayList<>();

        for (int i = 0; i < 9; i++){
            if (values.charAt(i) == 1){
                availableAbilities.add((availableAbilities.size() + 1) + ") " + abilities.get(i));
            }
        }

        return availableAbilities;}

}
