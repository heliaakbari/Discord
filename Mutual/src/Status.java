
public enum Status {
    ONLINE,
    IDLE,
    DO_NOT_DISTURB,
    INVISIBLE,
    OFFLINE;

//    public static Status defineStatus(String status){
//        if (status.equalsIgnoreCase("online"))
//            return ONLINE;
//        else if (status.equalsIgnoreCase("idle"))
//            return IDLE;
//        else if (status.equalsIgnoreCase("do not disturb"))
//            return DO_NOT_DISTURB;
//        else if (status.equalsIgnoreCase("invisible"))
//            return INVISIBLE;
//        else
//            return null;
//    }
//
//    public static Status defineStatus(int number){
//        return switch (number) {
//            case 1 -> ONLINE;
//            case 2 -> IDLE;
//            case 3 -> DO_NOT_DISTURB;
//            case 4 -> INVISIBLE;
//            default -> null;
//        };
//    }


}
