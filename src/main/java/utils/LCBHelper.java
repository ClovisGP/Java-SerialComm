package utils;

public class LCBHelper {
    public enum RequestKeyWords {
        END("END"),
        REQ("REQ"),
        ACK("ACK"),
        RES("RES"),
        NOT("NOT");

        private final String stringValue;
        RequestKeyWords(String stringValue) {
            this.stringValue = stringValue;
        }
        @Override
        public String toString() {
            return stringValue;
        }
    }
    public static String convertToTwoDigitsString(int nbr) {
        String doorString;
        if (nbr < 10)
            doorString = "0" + nbr;
        else
            doorString = String.valueOf(nbr);
        return doorString;
    }

    public static void cleanBeginMessage(StringBuilder str) {
        int index = 0;
        while (index < str.length() && str.charAt(index) == '\n') {
            index++;
        }
        str.delete(0, index);
    }
}
