import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MPv2TestDHT {
	
	private static final byte STX = (byte)253;
	private static final byte ADR = (byte)2;
	private static final byte FN = (byte)1;
	private static final byte GET = (byte)16;
	private static final byte LEN = (byte)1;
	private static final byte AFT_CMD = (byte)1;
	private static final byte AFH_CMD = (byte)2;
	private volatile boolean run = true;
	private volatile byte[] telegram = new byte[262];
	
	public static void main(String[] args) throws InterruptedException {
		MPv2TestDHT st = new MPv2TestDHT();
		st.getTemperature();
		st.getHumidity();
	}
	
	public void getTemperature() throws InterruptedException {
		communicate(ADR, AFT_CMD);
	}
	
	public void getHumidity() throws InterruptedException{
		communicate(ADR, AFH_CMD);
	}
	
	private void communicate(byte addr, byte plCmd) throws InterruptedException {
	
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
			rs485usb.setParity(2);
			rs485usb.setFlowControl(0);
			//addListener(rs485usb);
			rs485usb.openPort();
			try {
				byte [] crcRel = {addr,FN,GET,LEN,plCmd};
				long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
				//System.out.println(crcVal);
				// Nano => big-endian
				byte crcHigh = (byte) (crcVal & 0xFF);
				//System.out.println(crcHigh);
				byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
				//System.out.println(crcLow);
				byte [] telegram = {STX,addr,FN,GET,LEN,plCmd,crcHigh,crcLow};
				rs485usb.writeBytes(telegram, telegram.length);
				//System.out.println(this.telegram.length);
				while (rs485usb.bytesAvailable() == 0)
				Thread.sleep(20);

				byte[] readBuffer = new byte[rs485usb.bytesAvailable()];
				int numRead = rs485usb.readBytes(readBuffer, readBuffer.length);
				System.out.println("Read " + numRead + " bytes.");

				for(int i = 0; i < numRead; i++) {
					System.out.print(Byte.toUnsignedInt(readBuffer[i]) + " ");
				}

				System.out.println();
				byte[] crcArr = new byte [numRead-3];

				for (int i=1; i <= crcArr.length; i++) {
					crcArr[i-1] = readBuffer[i];
				}

				crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcArr);
				//System.out.println("CRC INT: " + crcVal);

				crcHigh = (byte) (crcVal & 0xFF);
				crcLow = (byte) ((crcVal & 0xFF00) >> 8);

				//System.out.println("CRC H: " +Byte.toUnsignedInt(crcHigh));
				//System.out.println("CRC L: " +Byte.toUnsignedInt(crcLow));
				if(crcHigh == readBuffer[readBuffer.length-2] && crcLow == readBuffer[readBuffer.length-1]) {
					String pre =  plCmd == AFT_CMD ? "Temperatur: " : "Luftfeuchtigkeit: ";
					String post =  plCmd == AFT_CMD ? "°C" : "%";
					System.out.println(pre + getValue(readBuffer) + post + "\n");
				}
			} catch (Exception e) { e.printStackTrace(); }
			//rs485usb.removeDataListener();
			rs485usb.closePort();
		}
	}
	
	float getValue(byte[] telegram) {
		return (float) (getBytesToInt(telegram[6], telegram[5]) / 100.0);
	}
		
	int getBytesToInt(byte high, byte low) {
		return (Byte.toUnsignedInt(high) << 8) | Byte.toUnsignedInt(low);
	}
	
	void addListener(SerialPort port) {
		port.addDataListener(new SerialPortDataListener() {
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
                    if(len < 0) {
                    	System.out.println(len);
                    	serialPortEvent.getSerialPort().readBytes(telegram, len);
                    	run = false;
                    }                      
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        });
	}
}

