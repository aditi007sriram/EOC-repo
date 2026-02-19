import java.util.HashMap;
import java.util.Map;

public class HackCPU {

    private int A = 0;
    private int D = 0;
    private int PC = 0;

    private enum Phase {
        FETCH,
        DECODE,
        EXECUTE,
        UPDATE_PC
    }
    private Phase currentPhase = Phase.FETCH;
    private String currentInstruction;


    private String[] program;

    private Map<String, Integer> symbolTable = new HashMap<>();
    private int nextVariableAddress = 16;
    private Map<String, Integer> RAM = new HashMap<>();


    public int getA() { return A; }
    public int getD() { return D; }
    public int getPC() { return PC; }

    public void reset() {
        A = 0;
        D = 0;
        PC = 0;
        stepCounter = 0;
        RAM.clear();
    }


    public void executeAInstruction(String value) {

        // If numeric
        if (value.matches("\\d+")) {
            A = Integer.parseInt(value);
        }

        // If symbol
        else {

            if (!symbolTable.containsKey(value)) {
                symbolTable.put(value, nextVariableAddress);
                nextVariableAddress++;
            }

            A = symbolTable.get(value);
        }

        PC++;
    }

    public void executeCInstruction(String instruction) {

        instruction = instruction.replace(" ", "");

        String dest = null;
        String comp;
        String jump = null;

        // Split jump if exists
        if (instruction.contains(";")) {
            String[] parts = instruction.split(";");
            instruction = parts[0];
            jump = parts[1];
        }

        // Split dest if exists
        if (instruction.contains("=")) {
            String[] parts = instruction.split("=");
            dest = parts[0];
            comp = parts[1];
        } else {
            comp = instruction;
        }

        // Evaluate computation
        int value = evaluateComp(comp);

        // Assign to destination if present
        if (dest != null) {
            if (dest.equals("D")) {
                D = value;
            } else if (dest.equals("M")) {
                RAM.put("R" + A, value);
            } else if (dest.equals("A")) {
                A = value;
            }
        }

        // Handle jump if present
        if (jump != null) {
            if (shouldJump(value, jump)) {
                PC = A;
            } else {
                PC++;
            }
        } else {
            PC++;
        }
    }

    private int evaluateComp(String comp) {

        switch (comp) {
            case "A": return A;
            case "D": return D;
            case "M": return RAM.getOrDefault("R" + A, 0);
            case "D+A": return D + A;
            case "D-M": return D - RAM.getOrDefault("R" + A, 0);
            case "0": return 0;
            default: return 0;
        }
    }

    private boolean shouldJump(int value, String jump) {

        switch (jump) {
            case "JEQ": return value == 0;
            case "JGT": return value > 0;
            case "JLT": return value < 0;
            case "JMP": return true;
            default: return false;
        }
    }

    private int stepCounter = 0;
    private static final int MAX_STEPS = 1000;



    public void loadProgram(String[] programLines) {

        symbolTable.clear();
        nextVariableAddress = 16;
        stepCounter = 0;
        PC = 0;

        // First pass: determine label addresses
        int instructionAddress = 0;

        for (String line : programLines) {

            line = line.trim();

            if (line.startsWith("(") && line.endsWith(")")) {
                String label = line.substring(1, line.length() - 1);
                symbolTable.put(label, instructionAddress);
            } else if (!line.isEmpty()) {
                instructionAddress++;
            }


        }

        // Second pass: build clean executable program (without labels)
        java.util.List<String> cleaned = new java.util.ArrayList<>();

        for (String line : programLines) {

            line = line.trim();

            if (line.isEmpty()) continue;

            if (line.startsWith("(") && line.endsWith(")")) continue;

            cleaned.add(line);
        }

        program = cleaned.toArray(new String[0]);
        System.out.println("Cleaned program:");
        for (int i = 0; i < program.length; i++) {
            System.out.println(i + " -> " + program[i]);
        }

    }


    public String step() {

        if (program == null || PC >= program.length) {
            return "Program finished.";
        }

        switch (currentPhase) {

            case FETCH:
                currentInstruction = program[PC];
                currentPhase = Phase.DECODE;
                return "FETCH → PC=" + PC + " Instruction=" + currentInstruction;

            case DECODE:
                currentPhase = Phase.EXECUTE;
                return "DECODE → " + currentInstruction;

            case EXECUTE:
                if (currentInstruction.startsWith("@")) {
                    executeAInstruction(currentInstruction.substring(1));
                } else {
                    executeCInstruction(currentInstruction);
                }
                currentPhase = Phase.UPDATE_PC;
                return "EXECUTE → Registers Updated";

            case UPDATE_PC:
                currentPhase = Phase.FETCH;
                return "UPDATE_PC → PC=" + PC;
        }

        return "";
    }



}
