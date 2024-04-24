import helpers.LCBHelper;
import interfaces.RequestCallback;
import perif.LCBComm;
import utils.LCBReqGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        LCBComm serialCommunication = new LCBComm("COM4");
        serialCommunication.openPort();
        RequestCallback callback = msgParsed -> {
            System.out.println(msgParsed.type + " " + (msgParsed.isError ? "ERROR" : "GOOD") + " " + msgParsed.payload);
            if (msgParsed.isError)
                return true;
            return msgParsed.type == LCBHelper.KeyWords.RES;
        };
        serialCommunication.sendRequest(LCBReqGenerator.RequestList.READ_CRC, callback);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 2);
        Thread.sleep(1000);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 3);
        //Thread.sleep(1000);
        serialCommunication.sendRequest(LCBReqGenerator.RequestList.READ_BATTERY, callback);
    }
}
