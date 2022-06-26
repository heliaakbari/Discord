public class databaseMain {
    public static void main(String[] args) {
        DatabaseManager dbm =new DatabaseManager();
        dbm.start();

        //dbm.newPvMsg(Command.newPvMsg("helia","ali",new TextMessage("helia","hellosss")));
        /*
        Relationship rel = Relationship.Friend_pending;
        rel.setSender("ali");
        rel.setReceiver("sara");
        dbm.newRelation(Command.newRelation(rel));
        */
    }


}
