import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MPv2TestCalibration {
	
	private static final byte STX = (byte)253;
	private static final byte ADR = (byte)1;
	private static final byte FN = (byte)1;
	private static final byte GET = (byte)16;
	private static final byte SET = (byte)32;
	private static final byte LEN = (byte)1;
	private static final byte CAL_CMD = (byte)4;
	private volatile boolean run = true;
	private volatile byte[] telegram = new byte[262];
	
	public static void main(String[] args) throws InterruptedException {
		MPv2TestCalibration st = new MPv2TestCalibration();
		st.getCalibration();
		//st.setCalibration();
	}
	
	public void getCalibration() throws InterruptedException {	
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
				final byte LEN = 1;
				byte [] crcRel = {ADR,FN,GET,LEN,CAL_CMD};
				long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
				//System.out.println(crcVal);
				// Nano => big-endian
				byte crcHigh = (byte) (crcVal & 0xFF);
				//System.out.println(crcHigh);
				byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
				//System.out.println(crcLow);
				byte [] telegram = {STX,ADR,FN,GET,LEN,CAL_CMD,crcHigh,crcLow};
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
					float tempOffset = (float) (getBytesToInt(readBuffer[6], readBuffer[5]) / 100.0);
					float humOffset = (float) (getBytesToInt(readBuffer[8], readBuffer[7]) / 100.0);
					System.out.println("T-Off: >" +  tempOffset + "< H-Off: >" + humOffset +"<\n");
				}
			} catch (Exception e) { e.printStackTrace(); }
			//rs485usb.removeDataListener();
			rs485usb.closePort();
		}
	}
	
	public void setCalibration() throws InterruptedException {	
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
				final byte LEN = 5;
				//double tOffset = 0.2;
				//double hOffset = 0.3;
				double tOffset = 0;
				double hOffset = 0;
				int t = (int) (tOffset * 100);
				int h = (int) (hOffset * 100);
				System.out.println(String.format("0x%08X", t));
				System.out.println(String.format("0x%08X", h));
				
				byte th = (byte)(t & 0xFF);
				byte tl = (byte)((t & 0xFF00) >> 8);
				byte hh = (byte)(h & 0xFF);
				byte hl = (byte)((h & 0xFF00) >> 8);
				
				
				byte [] crcRel = {ADR,FN,SET,LEN,CAL_CMD,th,tl,hh,hl};
				long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
				//System.out.println(crcVal);
				// Nano => big-endian
				byte crcHigh = (byte) (crcVal & 0xFF);
				//System.out.println(crcHigh);
				byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
				//System.out.println(crcLow);
				byte [] telegram = {STX,ADR,FN,SET,LEN,CAL_CMD,th,tl,hh,hl,crcHigh,crcLow};
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
					System.out.println("CRC ok:\n");
				}
			} catch (Exception e) { e.printStackTrace(); }
			//rs485usb.removeDataListener();
			rs485usb.closePort();
		}
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

