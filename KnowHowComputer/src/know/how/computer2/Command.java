package know.how.computer2;

public enum Command {
    INC("+"),
    DEC("-"),
    SPC("S"),
    EQZ("0"),
    STP("STOP");

    private final String symbol;

    Command(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
