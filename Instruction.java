public class Instruction {

    private OpCode opCode;
    private int operand;

    public Instruction(OpCode opCode, int operand) {
        this.opCode = opCode;
        this.operand = operand;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public int getOperand() {
        return operand;
    }

    public static void main(String[] args) {
        Instruction inst = new Instruction(OpCode.ADD, 10);
        System.out.println(inst.getOpCode() + " " + inst.getOperand());
    }
}
