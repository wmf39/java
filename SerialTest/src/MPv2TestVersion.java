import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class MPv2TestVersion {
	
	private static final byte STX = (byte)253;
	private static final byte ADR = (byte)42;
	private static final byte FN = (byte)1;
	private static final byte GET = (byte)16;
	private static final byte SET = (byte)32;
	private SerialPort rs485usb = null;
	
	public static void main(String[] args) throws InterruptedException {
		MPv2TestVersion st = new MPv2TestVersion();
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
			testGetVersion();
			//testSetVersion();
			rs485usb.closePort();
		}
	}
	
	void testGetVersion() {
		try {
			byte addr = 1;
			byte len = 1;
			byte getVersionCmd = 3;			
			byte [] crcRel = {addr,FN,GET,len,getVersionCmd};
			long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
			//System.out.println(crcVal);
			// Nano => big-endian
			byte crcHigh = (byte) (crcVal & 0xFF);
			//System.out.println(crcHigh);
			byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
			//System.out.println(crcLow);
			byte [] telegram = {STX,addr,FN,GET,len,getVersionCmd,crcHigh,crcLow};
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
		      
		      System.out.println();
		      byte[] crcArr = new byte [numRead-3];
		      
		      for (int i=1; i <= crcArr.length; i++) {
		    	  crcArr[i-1] = readBuffer[i];
		      }
		      
		      crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcArr);
		      				      
		      crcHigh = (byte) (crcVal & 0xFF);
		      crcLow = (byte) ((crcVal & 0xFF00) >> 8);
		      
		      if(crcHigh == readBuffer[readBuffer.length-2] && crcLow == readBuffer[readBuffer.length-1]) {
		    	  int major = Byte.toUnsignedInt(readBuffer[readBuffer.length-5]);
		    	  int minor = Byte.toUnsignedInt(readBuffer[readBuffer.length-4]);
		    	  int patch = Byte.toUnsignedInt(readBuffer[readBuffer.length-3]);
		    	  System.out.println("Version: " + major + "." + minor + "." + patch);
		      }
			      
			  run = false;
			  }
			} catch (Exception e) { e.printStackTrace(); }
	}
	
	void testSetVersion() {
		try {
			byte addr = 2;
			byte len = 4;
			byte setVersionCmd = 1;
			byte major = 2;
			byte minor = 0;
			byte patch = 0;;
			byte [] crcRel = {addr,FN,SET,len,setVersionCmd, major, minor, patch};
			long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
			//System.out.println(crcVal);
			// Nano => big-endian
			byte crcHigh = (byte) (crcVal & 0xFF);
			//System.out.println(crcHigh);
			byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
			//System.out.println(crcLow);
			byte [] telegram = {STX,addr,FN,SET,len,setVersionCmd,major,minor,patch,crcHigh,crcLow};
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

