import java.util.HashMap;
import java.util.Map;

public class SecurityValidator {

    private static final int MAX_INSTRUCTIONS = 1000;
    private static int instructionCount = 0;

    private static Map<OpCode, Integer> frequencyMap = new HashMap<>();

    public static boolean isSafe(Instruction instruction) {

        instructionCount++;

        if (instructionCount > MAX_INSTRUCTIONS) {
            System.out.println("Security Alert: Possible infinite loop detected.");
            return false;
        }

        OpCode op = instruction.getOpCode();
        int operand = instruction.getOperand();

        // Count frequency
        frequencyMap.put(op, frequencyMap.getOrDefault(op, 0) + 1);

        // Division by zero
        if (op == OpCode.DIV && operand == 0) {
            System.out.println("Security Alert: Division by zero detected.");
            return false;
        }

        // Operand abuse
        if (Math.abs(operand) > 1_000_000) {
            System.out.println("Security Alert: Operand value too large.");
            return false;
        }

        return true;
    }

    public static void reset() {
        instructionCount = 0;
        frequencyMap.clear();
    }

    public static Map<OpCode, Integer> getFrequencyMap() {
        return frequencyMap;
    }
}
