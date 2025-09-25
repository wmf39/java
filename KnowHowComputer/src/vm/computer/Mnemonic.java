package vm.computer;

public enum Mnemonic {
    INC("++"),
    DEC("--"),
    JMP("SPC"),
    EQZ("ISZ"),
    STR("ST"),
    STP("STOP");

    private final String symbol;

    Mnemonic(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
