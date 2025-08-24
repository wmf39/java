import java.nio.charset.StandardCharsets;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialTest {
	
	private static final byte STX = (byte)255;
	private volatile boolean run = true;
	
	public static void main(String[] args) throws InterruptedException {
		SerialTest st = new SerialTest();
		st.communicate();
	}
	public void communicate() throws InterruptedException{
	
		SerialPort [] comPorts = SerialPort.getCommPorts();
		SerialPort rs485usb = null;
		for(int i=0; i < comPorts.length; i++) {
			if(comPorts[i].getPortDescription().equals("USB Single Serial")) {
				rs485usb = comPorts[i];
				break;
			}
		}
		if(rs485usb != null) {
			rs485usb.setBaudRate(9600);
			rs485usb.setNumDataBits(8);
			rs485usb.setNumStopBits(1);
			rs485usb.setParity(0);
			rs485usb.setFlowControl(0);
			rs485usb.addDataListener(new SerialPortDataListener() {
	            @Override
	            public int getListeningEvents() {
	                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	            }

	            @Override
	            public void serialEvent(SerialPortEvent serialPortEvent) {

	                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE){
	                    //System.out.println("  event_type="+serialPortEvent.getEventType());
	                    return;
	                }

	                try {
	                    int len = serialPortEvent.getSerialPort().bytesAvailable();

	                    byte data[] = new byte[len];
	                    serialPortEvent.getSerialPort().readBytes(data, len);

	                    for (int i = 0; i < data.length; i++) {
	                        byte b = data[i];
	                        System.out.print(Byte.toUnsignedInt(b));
	                    }                       
	                    System.out.println();
	                    run = false;
	                } catch (Exception exp) {
	                    exp.printStackTrace();
	                }
	            }
	        });
			rs485usb.openPort();
			try {
				byte [] telegram = new byte[1];
				telegram[0] = STX;
				rs485usb.writeBytes(telegram, telegram.length);				 
				 
			} catch (Exception e) { 
				e.printStackTrace(); 
			}

			while(run) {};
			
			rs485usb.removeDataListener();
			rs485usb.closePort();
		}
	}
}