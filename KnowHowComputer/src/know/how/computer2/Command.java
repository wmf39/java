package know.how.computer2;

public enum Command {
    INC("+"),
    DEC("-"),
    SPC("SC"),
    EQZ("0"),
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
