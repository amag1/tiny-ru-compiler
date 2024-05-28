package codeGeneration;

import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public class MipsHelper {
    boolean debug;

    public MipsHelper(boolean debug) {
        this.debug = debug;
    }

    public String comment(String comment) {
        return debug ? "nop # " + comment : "";
    }

    public String generateMacros() {
        StringBuilder sb = new StringBuilder();
        sb.append(push());
        sb.append(pop());

        return sb.toString();
    }

    public String getLabel(MethodEntry method, ClassEntry entry) {
        return entry.getName() + "_" + method.getName() + ":";
    }

    public void appendTab(StringBuilder sb, String... elements) {
        sb.append("\t");
        for (String element : elements) {
            sb.append(element);
        }
        sb.append(System.lineSeparator());
    }

    public void append(StringBuilder sb, String... elements) {
        for (String element : elements) {
            sb.append(element);
        }
        sb.append(System.lineSeparator());
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
