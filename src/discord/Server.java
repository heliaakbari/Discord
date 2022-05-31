package discord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements Serializable {
    private String name;
    private HashMap<User, Role> members;
    private ArrayList<Channel> channels;

    public Server(String name) {
        this.name = name;
        members = new HashMap<>();
        channels = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<User, Role> getMembers() {
        return members;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }
}
