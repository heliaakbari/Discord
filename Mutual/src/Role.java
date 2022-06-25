
import java.util.ArrayList;
import java.util.Arrays;

public class Role {

    private String roleName;
    private String values;
    private final ArrayList<String> abilities = new ArrayList<>(Arrays.asList("creat channel", "remove channel", "remove member",
            "restrict member", "ban member", "change server name", "see chat history", "pin message", "delete server"));

    public Role(String values, String name) {
        this.values = values;
        this.roleName = name;
    }


    public ArrayList<String> getAvailableAbilities() {
        ArrayList<String> availableAbilities = new ArrayList<>();

        for (int i = 0; i < 9; i++){
            if (values.charAt(i) == 1){
                availableAbilities.add(abilities.get(i));
            }
        }

        return availableAbilities;
    }

    public String getRoleName() {
        return roleName;
    }
}
