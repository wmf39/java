import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MPv2TestAddress {
	
	private static final byte STX = (byte)253;
	private static final byte ADR = (byte)254;
	private static final byte FN = (byte)1;
	private static final byte GET = (byte)16;
	private static final byte SET = (byte)32;
	private SerialPort rs485usb = null;
	
	public static void main(String[] args) throws InterruptedException {
		MPv2TestAddress st = new MPv2TestAddress();
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
			testSetAddress();
			rs485usb.closePort();
		}
	}
		
	void testSetAddress() {
		try {
			byte slaveAddress = (byte)254;
			byte len = 2;
			byte setAddressCmd = 3;
			byte slaveAddressNew = (byte) 2;
			byte [] crcRel = {slaveAddress,FN,SET,len,setAddressCmd,slaveAddressNew};
			long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
			//System.out.println(crcVal);
			// Nano => big-endian
			byte crcHigh = (byte) (crcVal & 0xFF);
			//System.out.println(crcHigh);
			byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
			//System.out.println(crcLow);
			byte [] telegram = {STX,slaveAddress,FN,SET,len,setAddressCmd,slaveAddressNew,crcHigh,crcLow};
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

