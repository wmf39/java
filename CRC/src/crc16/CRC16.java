package crc16;

public class CRC16 {

	public int value;
	
	private Parameters params;

    public CRC16(Parameters params) {
        //value = 0;
    	value = params.getInit();
    }
    
    public void update(final byte [] bytes) {
    	for(byte b : bytes) {
    		update(b);
    	}   	
    }
    
    public void update(final byte aByte) {
        int a, b;

        a = (int) aByte;
        for (int count = 7; count >=0; count--) {
            a = a << 1;
            b = (a >>> 8) & 1;
            if ((value & 0x8000) != 0) {
                value = ((value << 1) + b) ^ 0x1021;
            } else {
                value = (value << 1) + b;
            }
        }
        value = value & 0xffff;
        return;
    }

    public void reset() {
        value = 0;
    }
}