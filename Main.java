public class Main {

    public static void main(String[] args) {

        System.out.println("=== Secure Visual VM Execution ===\n");

        // 1️⃣ Execute custom VM program
        VirtualMachine vm = new VirtualMachine();
        ProgramLoader.loadAndExecute("program.vm", vm);

        // 2️⃣ Analyze Java bytecode (cybersecurity feature)
        System.out.println("\n=== Java Bytecode Security Analysis ===");
        BytecodeAnalyzer.analyze("Test.class");
    }
}
