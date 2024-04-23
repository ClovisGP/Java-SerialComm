package utils;

/**
 * Manage the ID of the message of the LCB
 */
public class LCBMsgIdManager {
    int lastMsgId = 0;

    /**
     * Get the current ID
     * @return The current ID from 00 to FF
     */
    public String getCurrentId() {
        String hex = Integer.toHexString(lastMsgId);
        if (hex.length() == 1)
            hex = "0" + hex;
        return hex.toUpperCase();
    }

    /**
     * Increment the id. If it above 255, it returns to 0.
     */
    public void increment() {
        lastMsgId++;
        if (lastMsgId > 255)
            lastMsgId = 0;
    }
}
