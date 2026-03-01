public class HackALU {

    public static int compute(String comp, int A, int D, int M) {

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
}
