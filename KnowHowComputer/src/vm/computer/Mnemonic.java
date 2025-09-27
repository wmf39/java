package vm.computer;

public enum Mnemonic {
	
    INC("++"),
    DEC("--"),
    ADD("+"),
    MULT("-"),
    JMP("SPC"),
    EQZ("ISZ"),
    STR("ST"),
    POP("POP"),
    PUSH("PUSH"),
    CALL("CALL"),
    RET("RET"),
    STP("STOP");

    private final String symbol;

    Mnemonic(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
