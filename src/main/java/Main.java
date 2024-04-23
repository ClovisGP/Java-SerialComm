import perif.LCBComm;
import utils.LCBReqGenerator;

public class Main {
    public static void main(String[] args) {
        LCBComm serialCommunication = new LCBComm("COM4");
        serialCommunication.openPort();
        serialCommunication.sendRequest(LCBReqGenerator.RequestList.OPEN_DOOR, s -> System.out.println("Consumer 1: " + s), 1, 1);
        // Do your work
        //serialCommunication.closePort();
    }
}
