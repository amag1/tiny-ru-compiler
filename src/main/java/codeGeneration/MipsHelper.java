package codeGeneration;

public class MipsHelper {
    boolean debug;

    public MipsHelper(boolean debug) {
        this.debug = debug;
    }

    public String comment(String comment) {
        return debug ? "nop # " + comment + System.lineSeparator() : "";
    }

    public String generateMacros() {
        StringBuilder sb = new StringBuilder();
        sb.append(push());
        sb.append(pop());

        return sb.toString();
    }

    private String push() {
        StringBuilder sb = new StringBuilder();
        sb.append(".macro push (%r)").append(System.lineSeparator());
        sb.append("\tsw %r, ($sp)").append(System.lineSeparator());
        sb.append("\taddi $sp, $sp, -4").append(System.lineSeparator());
        sb.append(".end_macro").append(System.lineSeparator());

        return sb.toString();
    }

    private String pop() {
        StringBuilder sb = new StringBuilder();
        sb.append(".macro pop (%r)").append(System.lineSeparator());
        sb.append("\taddi $sp, $sp, 4").append(System.lineSeparator());
        sb.append("\tlw %r, ($sp)").append(System.lineSeparator());
        sb.append(".end_macro").append(System.lineSeparator());

        return sb.toString();
    }


}
