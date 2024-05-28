package codeGeneration;

import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public class MipsHelper {
    boolean debug;
    final private StringBuilder sb;

    public MipsHelper(boolean debug) {
        this.sb = new StringBuilder();
        this.debug = debug;
    }

    public void comment(String comment) {
        sb.append(debug ? "\tnop # " + comment + System.lineSeparator() : "");
    }

    public void startText() {
        sb.append(".text");
    }


    public String getString() {
        return sb.toString();
    }

    public void generateMacros() {
        sb.append(push());
        sb.append(pop());
    }

    public String getLabel(MethodEntry method, ClassEntry entry) {
        return entry.getName() + "_" + method.getName() + ":";
    }

    public void appendTab(String... elements) {
        sb.append("\t");
        for (String element : elements) {
            sb.append(element);
        }
        sb.append(System.lineSeparator());
    }

    public void append(String... elements) {
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

    public void lineSeparator() {
        append(System.lineSeparator());
    }

    public void move(String register1, String register2) {
        appendTab("move " + register1 +", "+ register2);
    }

    public void load(String register, String value) {
        appendTab("li " + register + ", " + value);
    }

    public void load(String register, int value) {
        appendTab("li " + register + ", " + value);
    }

    public void loadWord(String register1,  String register2) {
        appendTab("lw " + register1 +", "+ register2);
    }

    public void push(String register) {
        appendTab("push(" + register +")");
    }
}
