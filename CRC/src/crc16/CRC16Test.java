package crc16;

import reference.CRC;

public class CRC16Test {
	public static void main(String[] args) throws InterruptedException {

		final byte [] data = {42,16,1,1};
		
		int crcRef = (int) CRC.calculateCRC(CRC.Parameters.XMODEM, data);
		System.out.println(Integer.toHexString(crcRef));
		
		CRC16 crc = new CRC16(Parameters.TILLARD);
		crc.update(data);
		System.out.println(Integer.toHexString(crc.value));
		byte crcHigh = (byte) (crc.value & 0xFF);
		byte crcLow = (byte) ((crc.value & 0xFF00) >> 8);
		System.out.println("H:" + Integer.toHexString(Byte.toUnsignedInt(crcHigh)) + 
				" L:" + Integer.toHexString(Byte.toUnsignedInt(crcLow)));
	}
}
