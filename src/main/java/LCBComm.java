import com.fazecast.jSerialComm.*;
import utils.LCBHelper;
import utils.LCBReqGenerator;

public class LCBComm {
    private SerialPort mySerialPort;

    public LCBComm() {
        mySerialPort = SerialPort.getCommPort("COM4");
        mySerialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        mySerialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 15000, 1000);
    }

    public void openPort() {
        if (mySerialPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Unable to open the port.");
            return;
        }

        // Start the read thread
        Thread readThread = new Thread(() -> {
            StringBuilder incommingMessage = new StringBuilder();
            while (true) {
                byte[] readBuffer = new byte[1];
                int numBytes = mySerialPort.readBytes(readBuffer, readBuffer.length);
                if (numBytes > 0) {
                    char newChar = (char) (readBuffer[0] & 0xFF);
                    incommingMessage.append(newChar);
                    if (newChar == 'D' && incommingMessage.toString().contains(LCBHelper.RequestKeyWords.END.toString())) {
                        LCBHelper.cleanBeginMessage(incommingMessage);
                        System.out.println("New message => |" + incommingMessage + "|");
                        incommingMessage = new StringBuilder();
                    }

                }
            }
        });
        readThread.start();

        // Start the write thread
        Thread writeThread = new Thread(() -> {
            String data = LCBReqGenerator.generateReadBattery("00");
            mySerialPort.writeBytes(data.getBytes(), data.getBytes().length);

        });
        writeThread.start();
    }

    public void closePort() {
        if (mySerialPort.closePort()) {
            System.out.println("Port closed successfully.");
        } else {
            System.out.println("Unable to close the port.");
        }
    }



    public static void main(String[] args) {
        LCBComm serialCommunication = new LCBComm();
        serialCommunication.openPort();
        // Do your work
        //serialCommunication.closePort();
    }
}
