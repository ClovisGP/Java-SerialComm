package interfaces;
import utils.ParsersIncome;

/**
 * The callback interface of the LCB request
 */
public interface RequestCallback {
    /**
     * The execution function
     * @param msgParsed The Parsed message object
     * @return True if the request is finish. False if not
     */
    boolean run(ParsersIncome.IncomingMessageDecoded msgParsed);
}
