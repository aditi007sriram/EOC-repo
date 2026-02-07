public class InstructionParser {

    public static Instruction parse(String line) {

        line = line.trim();
        String[] parts = line.split("\\s+");

        OpCode opCode = OpCode.valueOf(parts[0]);

        int operand = 0;
        if (parts.length > 1) {
            operand = Integer.parseInt(parts[1]);
        }

        return new Instruction(opCode, operand);
    }
}
