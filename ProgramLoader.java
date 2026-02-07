import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ProgramLoader {

    public static void loadAndExecute(String filename, VirtualMachine vm) {

        // Reset security counters before execution
        SecurityValidator.reset();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse instruction
                Instruction instruction = InstructionParser.parse(line);

                // Security validation BEFORE execution
                if (!SecurityValidator.isSafe(instruction)) {
                    System.out.println("Execution halted due to security violation.");
                    return;
                }

                // Execute instruction
                vm.execute(instruction);
            }

            // Print instruction frequency report
            System.out.println("\nInstruction Frequency Report:");
            for (Map.Entry<OpCode, Integer> entry :
                    SecurityValidator.getFrequencyMap().entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

        } catch (IOException e) {
            System.out.println("Error reading program file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Runtime error: " + e.getMessage());
        }
    }
}
