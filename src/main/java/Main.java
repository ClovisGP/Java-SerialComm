import perif.LCBComm;
import utils.LCBReqGenerator;

public class Main {
    public static void main(String[] args) {
        LCBComm serialCommunication = new LCBComm("COM4");
        serialCommunication.openPort();
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 1);//Doesn't pass the arg
        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 2);
//        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 3);
//
//        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 4);//Doesn't pass the arg
//        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 5);
        //serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 6);
        // Do your work
        //serialCommunication.closePort();
    }
}
