package perif;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import com.fazecast.jSerialComm.*;
import helpers.LCBHelper;
import utils.LCBMsgIdManager;
import utils.LCBParserIncom;
import utils.LCBReqGenerator;


public class LCBComm {
    private final SerialPort LCBPort;
    private final Map<String, Consumer<String>> reqList = new HashMap<>();

    private final LCBMsgIdManager msgIdManager;

    private final Object mutex = new Object();


    public LCBComm(String portName) {
        msgIdManager = new LCBMsgIdManager();
        LCBPort = SerialPort.getCommPort(portName);
        LCBPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        LCBPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 15000, 1000);
    }

    public void openPort() {
        if (LCBPort.openPort()) {
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
                int numBytes = LCBPort.readBytes(readBuffer, readBuffer.length);
                if (numBytes > 0) {
                    char newChar = (char) (readBuffer[0] & 0xFF);
                    incommingMessage.append(newChar);
                    if (newChar == 'D' && incommingMessage.toString().contains(LCBHelper.KeyWords.END.toString())) {
                        LCBHelper.cleanBeginMessage(incommingMessage);

                        LCBParserIncom.IncomingMessageDecoded decodeMsg = LCBParserIncom.decode(incommingMessage.toString());
                        System.out.println(decodeMsg.id);
                        if (decodeMsg.type == LCBHelper.KeyWords.ACK || decodeMsg.type == LCBHelper.KeyWords.RES) {
                            if (reqList.get(decodeMsg.id) != null)
                                reqList.get(decodeMsg.id).accept(incommingMessage.toString());
                        } else {
                            System.out.println(incommingMessage);
                        }


                        incommingMessage = new StringBuilder();
                    }
                }
            }
        });
        readThread.start();
    }

    public void closePort() {
        if (LCBPort.closePort()) {
            System.out.println("Port closed successfully.");
        } else {
            System.out.println("Unable to close the port.");
        }
    }

    /**
     * Send a request to the LCB
     * @param request The identification of the request
     * @param callBack
     */
    public void sendRequest(LCBReqGenerator.RequestList request, Consumer<String> callBack, Object... reqArgs) { // Maybe improve the callBack
        Thread writeThread = new Thread(() -> {
            try {
                String data  = LCBReqGenerator.requestFunctions.get(request).request(msgIdManager.getCurrentId(), reqArgs);
                System.out.println("REQ => "+ data);
                LCBPort.writeBytes(data.getBytes(), data.getBytes().length);
                synchronized (mutex) {
                    reqList.put(msgIdManager.getCurrentId(), callBack);
                    msgIdManager.increment();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writeThread.start();


    }

}
