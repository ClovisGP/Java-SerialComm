package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import helpers.LCBHelper;

public class LCBReqGenerator {

    /**
     * The list of all possibly request
     */
    public enum RequestList {
        OPEN_DOOR,
        READ_DOOR,
        READ_BATTERY,
        ENTER_TESTMODE,
        READ_ISTESTMODE,
        RESET,
        READ_CELL,
        TARE_CELL;
    }
    /**
     * The interface of the prototype of the request
     */
    @FunctionalInterface
    public interface GenerateRequestFunction {
        String request(String messageId, Object... args) throws IOException;
    }

    public static final Map<RequestList, GenerateRequestFunction> requestFunctions = new HashMap<>();
    static {
        requestFunctions.put(RequestList.OPEN_DOOR, (messageId, args) -> generateOpenDoor(messageId, (int) args[0], (int) args[1]));
        requestFunctions.put(RequestList.READ_DOOR, (messageId, args) -> generateReadDoor(messageId, (int) args[0], (int) args[1]));
        requestFunctions.put(RequestList.READ_BATTERY, (messageId, args) -> generateReadBattery(messageId));
        requestFunctions.put(RequestList.ENTER_TESTMODE, (messageId, args) -> generateEnterTestMode(messageId));
        requestFunctions.put(RequestList.READ_ISTESTMODE, (messageId, args) -> generateCheckTestMode(messageId));
        requestFunctions.put(RequestList.RESET, (messageId, args) -> generateReset(messageId));
        requestFunctions.put(RequestList.READ_CELL, (messageId, args) -> generateReadCell(messageId));
        requestFunctions.put(RequestList.TARE_CELL, (messageId, args) -> generateTareCell(messageId));
    }



    /**
     * Generate the request to open one door
     * @param messageId The message ID
     * @param module The ID of the module.  1 < ID < 5
     * @param door The ID of the door for this module. 1 < ID < 12
     * @return The request
     */
    private static String generateOpenDoor(String messageId, int module, int door) throws IOException {
        /* Check validity */
        if (module < 1 || module > 5)
            throw new IOException("Invalid Module");
        if (door < 1 || door > 12)
            throw new IOException("Invalid Door");

        /* Request construction */
        String finalReq = messageId + "58";
        finalReq = finalReq + module + ';' + LCBHelper.convertToTwoDigitsString(door);
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to read the state one door
     * @param messageId The message ID
     * @param module The ID of the module.  1 < ID < 5
     * @param door The ID of the door for this module. 1 < ID < 12
     * @return The request
     */
    private static String generateReadDoor(String messageId, int module, int door) throws IOException {
        /* Check validity */
        if (module < 1 || module > 5)
            throw new IOException("Invalid Module");
        if (door < 1 || door > 12)
            throw new IOException("Invalid Door");

        /* Request construction */
        String finalReq = messageId + "57";
        finalReq = finalReq + module + ';' + LCBHelper.convertToTwoDigitsString(door);
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to read the state of the battery
     * @return The request
     */
    private static String generateReadBattery(String messageId) throws IOException {
        /* Request construction */
        String finalReq = messageId + "57";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to enter the test mode
     * @return The request
     */
    private static String generateEnterTestMode(String messageId) throws IOException {
        /* Request construction */
        String finalReq = messageId + "62";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to check if the test mode is activated
     * @return The request
     */
    private static String generateCheckTestMode(String messageId) throws IOException {
        /* Request construction */
        String finalReq = messageId + "63";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to reset
     * @return The request
     */
    private static String generateReset(String messageId) throws IOException {
        /* Request construction */
        String finalReq = messageId + "54";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to read the cell
     * @return The request
     */
    private static String generateReadCell(String messageId) throws IOException {
        /* Request construction */
        String finalReq = messageId + "55";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }

    /**
     * Generate the request to tare the cell
     * @return The request
     */
    private static String generateTareCell(String messageId) throws IOException {
        /* Request construction */
        String finalReq = messageId + "56";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.KeyWords.REQ + finalReq + LCBHelper.KeyWords.END;
    }
}
