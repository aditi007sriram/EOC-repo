import java.util.*;

public class HackCPU {

    private int A = 0;
    private int D = 0;
    private int PC = 0;
    private Set<String> labels = new HashSet<>();

    private String[] program;

    private Map<String, Integer> symbolTable = new HashMap<>();
    private int nextVariableAddress = 16;
    private Map<Integer, Integer> RAM = new HashMap<>();

    private void executeCInstruction(String instruction, ExecutionEvent event) {

        instruction = instruction.replace(" ", "");

        String dest = null;
        String comp;
        String jump = null;

        if (instruction.contains(";")) {
            String[] parts = instruction.split(";");
            instruction = parts[0];
            jump = parts[1];
        }

        if (instruction.contains("=")) {
            String[] parts = instruction.split("=");
            dest = parts[0];
            comp = parts[1];
        } else {
            comp = instruction;
        }

        event.oldPC = PC;
        event.oldA = A;
        event.oldD = D;

        // Evaluate ALU
        int value = 0;

        if (comp.equals("D+A")) {
            event.aluOperand1 = D;
            event.aluOperand2 = A;
            value = D + A;
        }
        else if (comp.equals("D-A")) {
            event.aluOperand1 = D;
            event.aluOperand2 = A;
            value = D - A;
        }
        else if (comp.equals("M")) {
            event.memoryReadAddress = A;
            value = RAM.getOrDefault(A, 0);
            event.aluOperand1 = value;
        }
        else if (comp.equals("D")) {
            value = D;
            event.aluOperand1 = D;
        }
        else if (comp.equals("A")) {
            value = A;
            event.aluOperand1 = A;
        }
        else if (comp.equals("0")) {
            value = 0;
            event.aluOperand1 = 0;
        }

        event.aluResult = value;

        // Destination write
        if (dest != null) {

            if (dest.equals("D")) {
                D = value;
            }
            else if (dest.equals("M")) {
                event.memoryWriteAddress = A;
                event.oldMemory = RAM.getOrDefault(A, 0);
                RAM.put(A, value);
                event.newMemory = value;
            }
            else if (dest.equals("A")) {
                A = value;
            }
        }

        event.newA = A;
        event.newD = D;

        // Jump handling
        if (jump != null) {
            event.jumpType = jump;
            event.jumpValue = value;

            boolean shouldJump = false;

            switch (jump) {
                case "JEQ": shouldJump = (value == 0); break;
                case "JGT": shouldJump = (value > 0); break;
                case "JLT": shouldJump = (value < 0); break;
                case "JMP": shouldJump = true; break;
            }

            event.jumpTaken = shouldJump;

            if (shouldJump)
                PC = A;
            else
                PC++;
        }
        else {
            PC++;
        }

        event.newPC = PC;
    }

    public void loadProgram(String[] programLines) {

        symbolTable.clear();
        nextVariableAddress = 16;
        PC = 0;
        RAM.clear();

        int instructionAddress = 0;

        // First pass: labels
        for (String line : programLines) {
            line = line.trim();
            if (line.startsWith("(") && line.endsWith(")")) {
                String label = line.substring(1, line.length() - 1);
                symbolTable.put(label, instructionAddress);
                labels.add(label);
            } else if (!line.isEmpty()) {
                instructionAddress++;
            }
        }


        // Second pass: remove labels
        List<String> cleaned = new ArrayList<>();
        for (String line : programLines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("(") && line.endsWith(")")) continue;
            cleaned.add(line);
        }

        program = cleaned.toArray(new String[0]);
    }

    public boolean isLabel(String name) {
        return labels.contains(name);
    }

    public Map<String, Integer> getSymbolTable() {
        return symbolTable;
    }

    public ExecutionEvent step() {

        ExecutionEvent event = new ExecutionEvent();

        if (program == null || PC >= program.length) {
            event.type = "FINISHED";
            return event;
        }

        String line = program[PC].trim();
        event.instruction = line;

        if (line.startsWith("@")) {

            event.type = "LOAD";

            event.oldA = A;
            event.oldPC = PC;

            String value = line.substring(1);

            if (value.matches("\\d+")) {
                A = Integer.parseInt(value);
            } else {
                if (!symbolTable.containsKey(value)) {
                    symbolTable.put(value, nextVariableAddress++);
                }
                A = symbolTable.get(value);
                event.variableName = value;
                event.address = A;
            }

            PC++;

            event.newA = A;
            event.newPC = PC;

            return event;
        }

        event.type = "C_INSTRUCTION";
        executeCInstruction(line, event);
        return event;
    }

    private int compute(String comp, int A, int D, int M) {

        switch (comp) {

            case "0": return 0;
            case "1": return 1;
            case "-1": return -1;

            case "D": return D;
            case "A": return A;
            case "M": return M;

            case "!D": return ~D;
            case "!A": return ~A;
            case "!M": return ~M;

            case "-D": return -D;
            case "-A": return -A;
            case "-M": return -M;

            case "D+1": return D + 1;
            case "A+1": return A + 1;
            case "M+1": return M + 1;

            case "D-1": return D - 1;
            case "A-1": return A - 1;
            case "M-1": return M - 1;

            case "D+A": return D + A;
            case "D+M": return D + M;

            case "D-A": return D - A;
            case "D-M": return D - M;

            case "A-D": return A - D;
            case "M-D": return M - D;

            case "D&A": return D & A;
            case "D&M": return D & M;

            case "D|A": return D | A;
            case "D|M": return D | M;

            default:
                throw new RuntimeException("Unknown comp: " + comp);
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
}