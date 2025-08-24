import com.fazecast.jSerialComm.SerialPort;
import java.util.concurrent.TimeUnit;
//import com.fazecast.jSerialComm.SerialPortDataListener;
//import com.fazecast.jSerialComm.SerialPortEvent;

public class MPv2Lasttest2Clients {
	
	private static final byte STX = (byte)253;
	private static final byte FN = (byte)1;
	private static final byte GET = (byte)16;
	private static final byte LEN = (byte)1;
	private static final byte D1 = (byte)1;
	
	public static void main(String[] args) throws InterruptedException {
		MPv2Lasttest2Clients st = new MPv2Lasttest2Clients();
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
			rs485usb.setParity(2);
			rs485usb.setFlowControl(0);
			rs485usb.openPort();
			try {
				byte addr = 1;
				byte [] crcRel = {addr,FN,GET,LEN,D1};
				long crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel);
				byte crcHigh = (byte) (crcVal & 0xFF);
				byte crcLow = (byte) ((crcVal & 0xFF00) >> 8);
				byte [] telegram1 = {STX,addr,FN,GET,LEN,D1,crcHigh,crcLow};
				
				addr = 2;
				byte [] crcRel2 = {addr,FN,GET,LEN,D1};
				crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcRel2);
				crcHigh = (byte) (crcVal & 0xFF);
				crcLow = (byte) ((crcVal & 0xFF00) >> 8);
				byte [] telegram2 = {STX,addr,FN,GET,LEN,D1,crcHigh,crcLow};
				
				long start = System.nanoTime();
				int trys = 5001, ok = 0, failed = 0;
				
				for(int z=0; z < trys; z++) {
					if((z+1)%2 == 1) {
						rs485usb.writeBytes(telegram1, telegram1.length);
					}
					else {
						rs485usb.writeBytes(telegram2, telegram2.length);
					}
					//while(run) {};
					//System.out.println(this.telegram.length);
					boolean run = true;
					while (run)
				    {
				      while (rs485usb.bytesAvailable() == 0)
				         Thread.sleep(20);
	
				      byte[] readBuffer = new byte[rs485usb.bytesAvailable()];
				      int numRead = rs485usb.readBytes(readBuffer, readBuffer.length);
				      //System.out.println("Read " + numRead + " bytes.");
				      
				      System.out.printf("%07d => ", (z+1));
				      for(int i = 0; i < numRead; i++) {
				    	  System.out.print(Byte.toUnsignedInt(readBuffer[i]) + " ");
				      }				      
				      System.out.println();
				      
				      byte[] crcArr = new byte [numRead-3];
				      
				      for (int j=1; j <= crcArr.length; j++) {
				    	  crcArr[j-1] = readBuffer[j];
				      }
				      crcVal = CRC.calculateCRC(CRC.Parameters.CCITT, crcArr);      				      
				      crcHigh = (byte) (crcVal & 0xFF);
				      crcLow = (byte) ((crcVal & 0xFF00) >> 8);
				      if(crcHigh == readBuffer[readBuffer.length-2] && crcLow == readBuffer[readBuffer.length-1]) {
				    	  ok++;
				      }
				      else {
				    	  failed++;
				      }
					  run = false;
					}
				}
				long duration = System.nanoTime() - start;
				System.out.print("Trys: " + trys + " Ok: " + ok + " Failed: " + failed);
				System.out.println(" Duration: " + TimeUnit.NANOSECONDS.toSeconds(duration) + "s");
			} catch (Exception e) { e.printStackTrace(); }
			rs485usb.closePort();
		}
	}
	
	float getValue(byte[] telegram) {
		return (float) (getBytesToInt(telegram[6], telegram[5]) / 100.0);
	}
		
	int getBytesToInt(byte high, byte low) {
		return (Byte.toUnsignedInt(high) << 8) | Byte.toUnsignedInt(low);
	}
}

