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
        //Thread.sleep(10000);
        serialCommunication.sendRequest(LCBReqGenerator.RequestList.READ_CRC, callback);
        Thread.sleep(1000);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 2);
        Thread.sleep(1000);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 3);
        Thread.sleep(1000);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 4);
        Thread.sleep(1000);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 5);
        Thread.sleep(1000);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, callback, 1, 6);
        //Thread.sleep(1000);
        serialCommunication.sendRequest(LCBReqGenerator.RequestList.READ_BATTERY, callback);
//        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 3);
//
//        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 4);//Doesn't pass the arg
//        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 5);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 6);
        // Do your work
        //serialCommunication.closePort();
    }
}
