package perif;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fazecast.jSerialComm.*;
import helpers.LCBHelper;
import utils.LCBMsgIdManager;
import utils.ParsersIncome;
import utils.LCBReqGenerator;
import interfaces.RequestCallback;


public class LCBComm {
    private final SerialPort LCBPort;
    private final Map<String, RequestCallback> reqList = new HashMap<>();

    private final LCBMsgIdManager msgIdManager;

    private final Object mutex = new Object();


    public LCBComm(String portName) {
        msgIdManager = new LCBMsgIdManager();
        LCBPort = SerialPort.getCommPort(portName);
        LCBPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        LCBPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 15000, 1000);
    }

    /**
     * Read/parse the incoming message
     * @param msg
     */
    private void ReadIncomingMsg(String msg) {
        ParsersIncome.IncomingMessageDecoded decodeMsg = ParsersIncome.decode(msg);
        System.out.println(decodeMsg.id);
        if (decodeMsg.type == LCBHelper.KeyWords.ACK || decodeMsg.type == LCBHelper.KeyWords.RES) {
            if (reqList.get(decodeMsg.id) != null) {
                if (reqList.get(decodeMsg.id).run(decodeMsg)) {
                    synchronized (mutex) { reqList.remove(decodeMsg.id);}
                }
            }
        } else {
            System.out.println(msg);
        }
    }

    /**
     * Open the port and the listener
     */
    public void openPort() throws IOException {
        if (!LCBPort.openPort())
            throw new IOException("Unable to open the port");

        /* Reader Thread */
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

                        /* Async Reading */
                        final String strToRead = incommingMessage.toString(); // Make a copy and final to avoid the erasing and a warning
                        Thread asyncReading = new Thread(() -> {
                            ReadIncomingMsg(strToRead);
                        });
                        asyncReading.start();

                        incommingMessage = new StringBuilder();
                    }
                }
            }
        });
        readThread.start();

        //check CRC
        //remove it
        //Reset

        //reqList.put(msgIdManager.getCurrentId(), () => {});

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
     * @param callBack The callBack
     * @param reqArgs The request arguments
     */
    public void sendRequest(LCBReqGenerator.RequestList request, RequestCallback callBack, Object... reqArgs) {
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
