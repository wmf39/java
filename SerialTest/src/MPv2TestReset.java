import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MPv2TestReset {
	
	private static final byte STX = (byte)253;
	private static final byte ADR = (byte)1;
	private static final byte FN = (byte)1;
	private static final byte SET = (byte)32;
	private SerialPort rs485usb = null;
	
	public static void main(String[] args) throws InterruptedException {
		MPv2TestReset st = new MPv2TestReset();
		st.communicate();
	}
	public void communicate() throws InterruptedException {
	
		SerialPort [] comPorts = SerialPort.getCommPorts();
		
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
			rs485usb.openPort();
			testSetReset();
			rs485usb.closePort();
		}
	}
		
	void testSetReset() {
		try {
			byte len = 1;
			byte setResetCmd = 2;
			byte [] crcRel = {ADR,FN,SET,len,setResetCmd};
			long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
			//System.out.println(crcVal);
			// Nano => big-endian
			byte crcHigh = (byte) (crcVal & 0xFF);
			//System.out.println(crcHigh);
			byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
			//System.out.println(crcLow);
			byte [] telegram = {STX,ADR,FN,SET,len,setResetCmd,crcHigh,crcLow};
			rs485usb.writeBytes(telegram, telegram.length);
			
			
			boolean run = true;
			while (run)
		    {
		      while (rs485usb.bytesAvailable() == 0)
		         Thread.sleep(20);

		      byte[] readBuffer = new byte[rs485usb.bytesAvailable()];
		      int numRead = rs485usb.readBytes(readBuffer, readBuffer.length);
		      System.out.println("Read " + numRead + " bytes.");
		      
		      for(int i = 0; i < numRead; i++) {
		    	  System.out.print(Byte.toUnsignedInt(readBuffer[i]) + " ");
		      }			      
		      run = false;
			  }
			  
			} catch (Exception e) { e.printStackTrace(); }
	}
}

