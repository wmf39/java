import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialTest2 {
	
	private static final byte STX = (byte)255;
	private static final byte ETX = (byte)254;
	private static final byte AFT = (byte)253;
	private static final byte AFH = (byte)252;
	private volatile boolean run = true;
	private volatile byte[] telegram = new byte[5];
	
	public static void main(String[] args) throws InterruptedException {
		SerialTest2 st = new SerialTest2();
		st.communicate();
	}
	public void communicate() throws InterruptedException {
	
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
	                    //System.out.println("EventType=" + serialPortEvent.getEventType());
	                    return;
	                }

	                try {
	                    int len = serialPortEvent.getSerialPort().bytesAvailable();
	                    if(telegram.length == len) {
	                    	serialPortEvent.getSerialPort().readBytes(telegram, len);
	                    	run = false;
	                    }                      
	                } catch (Exception exp) {
	                    exp.printStackTrace();
	                }
	            }
	        });
			rs485usb.openPort();
			try {
				byte [] telegram = new byte[1];
				telegram[0] = AFT;
				rs485usb.writeBytes(telegram, telegram.length);
				while(run) {};
				if(isValidTelegram()) {
					float temperature = getTemperature();
					System.out.println("Temperature: " + temperature);		
				}
				Thread.sleep(5000);
				telegram[0] = AFH;
				run = true;
				rs485usb.writeBytes(telegram, telegram.length);
				while(run) {};
				if(isValidTelegram()) {
					float humidity = getHumidity();
					System.out.println("Humidity: " + humidity);		
				}				 
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
			rs485usb.removeDataListener();
			rs485usb.closePort();
		}
	}
	
	boolean isValidTelegram() {
		byte bcc = 0;
		for (int i = 1; i < (telegram.length-2); i++) {
			bcc ^= telegram[i];
		}
		return STX == telegram[0] && bcc == telegram[3] && ETX == telegram[4];
	}
	
	float getTemperature() {
		return (float) (getBytesToInt(telegram[2], telegram[1]) / 100.0);
	}
	
	float getHumidity() {
		return (float) (getBytesToInt(telegram[2], telegram[1]) / 100.0);
	}
	
	int getBytesToInt(byte high, byte low) {
		return (Byte.toUnsignedInt(high) << 8) | Byte.toUnsignedInt(low);
	}
}