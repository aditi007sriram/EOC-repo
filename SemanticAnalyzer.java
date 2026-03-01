public class SemanticAnalyzer {

    public static String analyze(ExecutionEvent e) {

        if (e.type == null)
            return "Instruction executed.";

        if (e.type.equals("FINISHED"))
            return "Program execution completed.";

        StringBuilder sb = new StringBuilder();

        sb.append("Instruction: ").append(e.instruction).append("\n");

        if (e.type.equals("LOAD")) {

            sb.append("LOAD PHASE:\n");

            if (e.variableName != null) {
                sb.append("Symbol '").append(e.variableName)
                        .append("' resolved to address ")
                        .append(e.address).append("\n");
            }

            sb.append("Register A updated from ")
                    .append(e.oldA).append(" to ")
                    .append(e.newA).append("\n");

            sb.append("Program Counter moved from ")
                    .append(e.oldPC).append(" to ")
                    .append(e.newPC);

            return sb.toString();
        }

        if (e.type.equals("C_INSTRUCTION")) {

            sb.append("EXECUTION PHASE:\n");

            if (e.memoryReadAddress != null) {
                sb.append("Memory Read: RAM[")
                        .append(e.memoryReadAddress)
                        .append("] = ")
                        .append(e.aluOperand1)
                        .append("\n");
            }

            if (e.aluOperand2 != 0 || e.aluOperand1 != e.aluResult) {
                sb.append("ALU Operation: ")
                        .append(e.aluOperand1)
                        .append(" and ")
                        .append(e.aluOperand2)
                        .append(" → Result = ")
                        .append(e.aluResult)
                        .append("\n");
            }

            if (e.memoryWriteAddress != null) {
                sb.append("Memory Write: RAM[")
                        .append(e.memoryWriteAddress)
                        .append("] updated from ")
                        .append(e.oldMemory)
                        .append(" to ")
                        .append(e.newMemory)
                        .append("\n");
            }

            if (e.jumpType != null) {
                sb.append("Jump Evaluation:\n");
                sb.append("Condition: ").append(e.jumpType).append("\n");
                sb.append("Compared Value: ").append(e.jumpValue).append("\n");
                sb.append("Jump Taken: ").append(e.jumpTaken).append("\n");
            }

            sb.append("PC updated from ")
                    .append(e.oldPC)
                    .append(" to ")
                    .append(e.newPC);

            return sb.toString();
        }

        return "Instruction executed.";
    }
}