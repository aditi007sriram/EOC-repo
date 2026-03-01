public class HackTest {

    public static void main(String[] args) {

        HackCPU cpu = new HackCPU();

        String[] program = {

                "@3",
                "D=A",
                "@x",
                "M=D",

                "(LOOP)",

                "@x",
                "D=M",

                "@END",
                "D;JEQ",

                "@x",
                "D=M",
                "@1",
                "D=D-A",
                "@x",
                "M=D",

                "@LOOP",
                "0;JMP",

                "(END)"
        };

        cpu.loadProgram(program);

        while (true) {

            ExecutionEvent event = cpu.step();

            String explanation = SemanticAnalyzer.analyze(event);
            System.out.println("Instruction: " + event.instruction);
            System.out.println(explanation);
            System.out.println("--------------------------------");

            if (event.type.equals("FINISHED"))
                break;
        }
    }
}