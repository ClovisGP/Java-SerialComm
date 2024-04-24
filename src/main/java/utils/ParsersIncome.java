package utils;

import helpers.LCBHelper;

public class ParsersIncome {

    /**
     * A decode message
     */
    public static class IncomingMessageDecoded {
        /**
         * The type
         */
        public LCBHelper.KeyWords type;

        /**
         * The id, from 00 to FF. And aa to ZZ for NOT and it is different.
         */
        public String id;

        /**
         * The CRC in char format
         */
        public char crc;

        /**
         * Is the message is an error. Use by ACK and RES
         */
        public boolean isError = false;

        /**
         * Is the board id. Use by NOT
         */
        public String boardId;

        /**
         * The content of the message or all the message if there is an error
         */
        public String payload = "";
    }

    /**
     * Parse an Acknowledgement Structure
     */
    private static void parseACK(String incomingMsg, IncomingMessageDecoded returnObj) {
    System.out.println(incomingMsg);
        returnObj.id = incomingMsg.substring(3, 5);
        System.out.println("ID => " + returnObj.id);
        if (incomingMsg.charAt(5) == '0')
            returnObj.isError = true;
        returnObj.payload = String.valueOf(incomingMsg.charAt(7));
        returnObj.crc = incomingMsg.charAt(8);
    }

    /**
     * Parse a Response Structure
     */
    private static void parseRES(String incomingMsg, IncomingMessageDecoded returnObj) {
        returnObj.id = incomingMsg.substring(3, 5);
        if (incomingMsg.charAt(5) == '0')
            returnObj.isError = true;
        returnObj.payload = incomingMsg.substring(6, incomingMsg.length() - 4);
        returnObj.crc = incomingMsg.charAt(incomingMsg.length() - 4);
    }

    /**
     * Parse a Response Structure
     */
    private static void parseNOT(String incomingMsg, IncomingMessageDecoded returnObj) {
        returnObj.boardId = incomingMsg.substring(3, 7);
        returnObj.id = incomingMsg.substring(7, 9);
        returnObj.payload = incomingMsg.substring(6, incomingMsg.length() - 4);
        returnObj.crc = incomingMsg.charAt(incomingMsg.length() - 4);
    }

    /**
     * Decode the incoming message in a IncomingMessageDecoded
     * @param incomingMsg The source message
     * @return IncomingMessageDecoded
     */
    public static IncomingMessageDecoded decode(String incomingMsg) {
        IncomingMessageDecoded returnObj = new IncomingMessageDecoded();
        if (incomingMsg.length() < 6) {
            returnObj.type = LCBHelper.KeyWords.END; // Use to find error
            returnObj.payload = incomingMsg;
            return returnObj;
        }
        LCBHelper.KeyWords keyword = LCBHelper.KeyWords.fromString(incomingMsg.substring(0, 3));
        if (keyword != null) {
            returnObj.type = keyword;
            switch (keyword) {
                case ACK:
                    parseACK(incomingMsg, returnObj);
                    break;
                case RES:
                    parseRES(incomingMsg, returnObj);
                    break;
                case NOT:
                    parseNOT(incomingMsg, returnObj);
                    break;
                default:
                    returnObj.payload = incomingMsg;
            }
        } else {
            returnObj.type = LCBHelper.KeyWords.END; // Use to find error
            returnObj.payload = incomingMsg;
        }
        return returnObj;
    }
}
