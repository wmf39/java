package vm.computer;

public enum Command {
    INC("++"),
    DEC("--"),
    JMP("SPC"),
    EQZ("ISZ"),
    STR("ST"),
    STP("STOP");

    private final String symbol;

    Command(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
