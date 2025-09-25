package vm.computer;

public class Instruction<A, B, C> {
	public final A mnemonic;
	public final B target;
    public final C source;

    public Instruction(A mnemonic) {
    	this(mnemonic, null, null);
    }
    
    public Instruction(A mnemonic, B target) {
    	this(mnemonic, target, null);
    }
    
    public Instruction(A mnemonic, B target, C source) {
    	this.mnemonic = mnemonic;
    	this.target = target;
    	this.source = source;
    }

    @Override
    public String toString() {
        return "(" + mnemonic + ", " + target + ", " + source + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Instruction<?, ?, ?> other)) return false;
        return mnemonic.equals(other.mnemonic) && target.equals(other.target) && source.equals(other.source);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(mnemonic, target, source);
    }
}

