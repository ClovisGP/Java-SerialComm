package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import utils.LCBHelper;

public class LCBReqGenerator {
    /**
     * Generate the request to open one door
     * @param messageId The message ID
     * @param module The ID of the module.  1 < ID < 5
     * @param door The ID of the door for this module. 1 < ID < 12
     * @return The request
     */
    public static String generateOpenDoor(String messageId, int module, int door) throws IOException {
        /* Check validity */
        if (module < 1 || module > 5)
            throw new IOException("Invalid Module");
        if (door < 1 || door > 12)
            throw new IOException("Invalid Door");

        /* Request construction */
        String finalReq = messageId + "58";
        finalReq = finalReq + module + ';' + LCBHelper.convertToTwoDigitsString(door);
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to read the state one door
     * @param messageId The message ID
     * @param module The ID of the module.  1 < ID < 5
     * @param door The ID of the door for this module. 1 < ID < 12
     * @return The request
     */
    public static String generateReadDoor(String messageId, int module, int door) throws IOException {
        /* Check validity */
        if (module < 1 || module > 5)
            throw new IOException("Invalid Module");
        if (door < 1 || door > 12)
            throw new IOException("Invalid Door");

        /* Request construction */
        String finalReq = messageId + "57";
        finalReq = finalReq + module + ';' + LCBHelper.convertToTwoDigitsString(door);
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to read the state of the battery
     * @return The request
     */
    public static String generateReadBattery(String messageId) {
        /* Request construction */
        String finalReq = messageId + "57";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to enter the test mode
     * @return The request
     */
    public static String generateEnterTestMode(String messageId) {
        /* Request construction */
        String finalReq = messageId + "62";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to check if the test mode is activated
     * @return The request
     */
    public static String generateCheckTestMode(String messageId) {
        /* Request construction */
        String finalReq = messageId + "63";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to reset
     * @return The request
     */
    public static String generateReset(String messageId) {
        /* Request construction */
        String finalReq = messageId + "54";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to read the cell
     * @return The request
     */
    public static String generateReadCell(String messageId) {
        /* Request construction */
        String finalReq = messageId + "55";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }

    /**
     * Generate the request to tare the cell
     * @return The request
     */
    public static String generateTareCell(String messageId) {
        /* Request construction */
        String finalReq = messageId + "56";
        finalReq = finalReq + (char) (CRC8Manager.compute(finalReq.getBytes(StandardCharsets.UTF_8)) & 0xFF);
        return LCBHelper.RequestKeyWords.REQ + finalReq + LCBHelper.RequestKeyWords.END;
    }
}
