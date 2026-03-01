public class HackParser {

    public static HackInstruction parse(String line) {

        line = line.trim();

        // A-instruction
        if (line.startsWith("@")) {
            String symbol = line.substring(1);
            return new HackInstruction(symbol);
        }

        // C-instruction
        String dest = null;
        String comp = null;
        String jump = null;

        if (line.contains(";")) {
            String[] parts = line.split(";");
            line = parts[0];
            jump = parts[1];
        }

        if (line.contains("=")) {
            String[] parts = line.split("=");
            dest = parts[0];
            comp = parts[1];
        } else {
            comp = line;
        }

        return new HackInstruction(dest, comp, jump);
    }
}
