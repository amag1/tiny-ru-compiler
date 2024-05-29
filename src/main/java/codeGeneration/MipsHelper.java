package codeGeneration;

import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;
import semantic.symbolTable.VariableEntry;

import java.util.List;

public class MipsHelper {
    boolean debug;
    final private StringBuilder sb;

    static private String nilPointer = "0x00000000";

    public MipsHelper(boolean debug) {
        this.sb = new StringBuilder();
        this.debug = debug;
    }

    public void comment(String comment) {
        sb.append(debug ? "\tnop # " + comment + System.lineSeparator() : "");
    }

    public void startText() {
        append(".text");
    }

    public void startData() {
        append(".data");
    }


    public String getString() {
        return sb.toString();
    }

    public void generateMacros() {
        sb.append(push());
        sb.append(pop());

    }

    public String getLabel(String method, String entry) {
        return entry + "_" + method;
    }

    private void appendTab(String... elements) {
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

    public void addIU(String register1, String register2, int offset) {
        appendTab("addiu " + register1 + ", " + register2 + ", " + offset);
    }

    public void jumpRegister(String register) {
        appendTab("jr " + register);
    }

    public void branchOnEqual(String register1, String register2, String offset) {
        appendTab("beq " + register1 + ", " + register2 + ", " + offset);
    }

    public void jump(String address) {
        appendTab("j " + address);
    }

    public void loadAddress(String register, String label) {
        appendTab("la " + register + ", " + label);
    }

    public  void storeInAccumulator(String address) {
        loadAddress("$a0", address);
    }

    public void allocateMemory(int numberOfBits) {
        this.comment("AllocateMemory");
        this.load("$a0", numberOfBits); // Set number of bits to allocate
        this.syscall(9);
        this.move("$a0", "$v0"); // Store in accumulator the address of the result
    }

    public void syscall(int number) {
        this.load("$v0", number);
        this.appendTab( "syscall");
    }

    public void initMethod(MethodEntry method, ClassEntry classEntry) {
        startText();
        comment("init Method");

        append(getLabel(method.getName(), classEntry.getName()) + ":");

        // Pushea local vars
        for (VariableEntry var:method.getLocalVarList()) {
            // TODO
        }

        // Pushear registro de activación
        push("$ra");
    }

    public void finishMethod() {
        loadWord("$ra", "4($sp)"); // Restore return addres
        move("$sp", "$fp"); // Reset stack pointer
        loadAddress("$fp", "($fp)"); // Restore frame pointer
        jumpRegister("$ra"); // Return to calling instruction
    }


    /**
     *
     * @param method
     * @return el offset necesario para acceder al un parametro en el stack
     * desde el frame pointer
     */
    public int getStackParamOffset(MethodEntry method, int pararameterPosition) {
        int offset = 0;

        // Agrega frame pointer del llamador
        offset -= 4;

        // Agrega self si el método no es estatico
        if (!method.isStatic()) {
            offset -= 4;
        }

        // Agrega parámtetros anteriores
        offset -= (4*pararameterPosition);

        return offset;
    }

    /**
     *
     * @param method
     * @return el offset necesario para acceder a una  variable local en el
     * stack desde el frame pointer
     */
    public int getStackLocalVarOffset(MethodEntry method, int varPosition) {
        int offset = 0;

        // Agrega frame pointer del llamador
        offset -= 4;

        // Agrega self si el método no es estatico
        if (!method.isStatic()) {
            offset -= 4;
        }

        // Agrega los parametros
        offset -= method.getFormalParametersList().toArray().length;

        // Agrega variables anteriores
        offset -= (4*varPosition);

        return  offset;
    }

    public void addDataLabel(String name, String type, String value) {
        if (!name.equals("")) name += ":";
        appendTab(name + " " + type + " " + value);
    }

    public String getVirtualTableName(ClassEntry classEntry) {
        return "VT_" + classEntry.getName();
    }

    public void updateFramePointer() {
        push("$fp"); // Guarda en stack el fp anterior
        loadWord("$fp", "4($sp)"); // Guarda en fp donde comenzó el stack pointer
    }

    public void jumpAndLinkRegister(String register){
        append("jalr " + register);
    }

    public String getDefaultType(AttributeType attType) {
        if (attType.isArray() || !attType.isPrimitive()) {
            return nilPointer;
        }

        switch (attType.getType()) {
            case "Int":
                return "$zero";
            case "Str":
                return  "\"\"";
            case "Bool":
                return  "$zero";
            case "Char":
                return  "' '";
        }

        return nilPointer;
    }

}
