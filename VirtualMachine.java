public class VirtualMachine {

    private int accumulator = 0;

    public void execute(Instruction instruction) {

        switch (instruction.getOpCode()) {

            case ADD:
                accumulator += instruction.getOperand();
                break;

            case SUB:
                accumulator -= instruction.getOperand();
                break;

            case MUL:
                accumulator *= instruction.getOperand();
                break;

            case DIV:
                if (instruction.getOperand() == 0) {
                    System.out.println("Security Alert: Division by zero detected.");
                    return;
                }
                accumulator /= instruction.getOperand();
                break;

            case PRINT:
                System.out.println("ACC = " + accumulator);
                break;
        }
    }

    // ✅ IMPORTANT: Getter method for GUI
    public int getAccumulator() {
        return accumulator;
    }

    // Optional reset method (good practice)
    public void reset() {
        accumulator = 0;
    }
}
