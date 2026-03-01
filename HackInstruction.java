public class HackInstruction {

    public enum Type {
        A_INSTRUCTION,
        C_INSTRUCTION
    }

    private Type type;

    private String symbol;  // for A-instruction
    private String dest;
    private String comp;
    private String jump;

    // Constructor for A-instruction
    public HackInstruction(String symbol) {
        this.type = Type.A_INSTRUCTION;
        this.symbol = symbol;
    }

    // Constructor for C-instruction
    public HackInstruction(String dest, String comp, String jump) {
        this.type = Type.C_INSTRUCTION;
        this.dest = dest;
        this.comp = comp;
        this.jump = jump;
    }

    public Type getType() { return type; }
    public String getSymbol() { return symbol; }
    public String getDest() { return dest; }
    public String getComp() { return comp; }
    public String getJump() { return jump; }
}
