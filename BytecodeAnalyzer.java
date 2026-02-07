import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BytecodeAnalyzer {

    public static void analyze(String classFile) {

        try {
            Process process = Runtime.getRuntime()
                    .exec("javap -c " + classFile);

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            System.out.println("\nBytecode Analysis Report:");
            while ((line = reader.readLine()) != null) {

                if (line.contains("goto")) {
                    System.out.println("Warning: Jump instruction detected (possible loop)");
                }

                if (line.contains("invokestatic")) {
                    System.out.println("Info: Static method call detected");
                }
            }

        } catch (Exception e) {
            System.out.println("Bytecode analysis failed: " + e.getMessage());
        }
    }
}
