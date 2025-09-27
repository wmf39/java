package vm.computer;

public class Instruction<A, B, C, D> {
	
	public final A mnemonic;
	public final B operand1;
    public final C operand2;
    public final D operand3;

    public Instruction(A mnemonic) {
    	this(mnemonic, null);
    }
    
    public Instruction(A mnemonic, B operand1) {
    	this(mnemonic, operand1, null);
    }
    
    public Instruction(A mnemonic, B operand1, C operand2) {
    	this(mnemonic, operand1, operand2, null);
    }

    public Instruction(A mnemonic, B operand1, C operand2, D operand3) {
    	this.mnemonic = mnemonic;
    	this.operand1 = operand1;
    	this.operand2 = operand2;
    	this.operand3 = operand3;
    }
    
    @Override
    public String toString() {
        return "(" + mnemonic + ", " + operand1 + ", " + operand2 + ", " + operand3 + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Instruction<?, ?, ?, ?> other)) return false;
        return mnemonic.equals(other.mnemonic) && operand1.equals(other.operand1) 
        		&& operand2.equals(other.operand2) && operand3.equals(other.operand3);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(mnemonic, operand1, operand2, operand3);
    }
}

