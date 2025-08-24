package crc16;

public enum Parameters {
	
	TILLARD(0x8001, 0x0000, 0x0), 
	CCITT(0x1021, 0x0000, 0x0);
	
	private int polynom;
	private int init;
	private int xorOut;

	Parameters(int polynom, int init, int xorOut) {
		this.polynom = polynom;
		this.init = init;
		this.xorOut = xorOut;
	}

    public int getPolynomial()
    {
        return polynom;
    }

    public int getInit()
    {
        return init;
    }

    public int getFinalXor()
    {
        return xorOut;
    }
}
