package discord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Channel implements Serializable {
    private String name;
    private HashMap<Message, HashMap<String, Integer>> messagesAndReactions;
    private ArrayList<Message> pins;
    private ArrayList<User> restricts;
    private LinkedList<User> presentMembers;

    public Channel(String name, HashMap<User, Role> members) {
        this.name = name;


        messagesAndReactions = new HashMap<>();
        pins = new ArrayList<>();
        presentMembers = new LinkedList<>();
    }

    //  member should be added to present members whenever enter channel method is invoked in class Server


    public String getName() {
        return name;
    }

    public HashMap<Message, HashMap<String, Integer>> getMessagesAndReactions() {
        return messagesAndReactions;
    }

    public ArrayList<Message> getPins() {
        return pins;
    }

    public ArrayList<User> getRestricts() {
        return restricts;
    }

    public LinkedList<User> getPresentMembers() {
        return presentMembers;
    }
}
