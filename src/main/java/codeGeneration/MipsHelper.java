package codeGeneration;

import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;
import semantic.symbolTable.VariableEntry;

import java.util.List;
import java.util.Map;

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
        appendTab("move " + register1 + ", " + register2);
    }

    public void load(String register, String value) {
        appendTab("li " + register + ", " + value);
    }

    public void load(String register, int value) {
        appendTab("li " + register + ", " + value);
    }

    public void loadWord(String register1, String register2) {
        appendTab("lw " + register1 + ", " + register2);
    }

    public void push(String register) {
        appendTab("push(" + register + ")");
    }

    public void pop(String register) {
        appendTab("pop(" + register + ")");
    }

    public void addIU(String register1, String register2, int offset) {
        appendTab("addiu " + register1 + ", " + register2 + ", " + offset);
    }

    public void add(String register1, String register2, String register3) {
        appendTab("add " + register1 + ", " + register2 + ", " + register3);
    }

    public void sw(String registerWithValue, String address) {
        appendTab("sw " + registerWithValue + ", " + address);
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

    public void storeInAccumulator(String address) {
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
        this.appendTab("syscall");
    }

    public void mutilply(String resRegister, String opRegister1, String opRegister2) {
        append("mul " + resRegister + ", " + opRegister1 + ", " + opRegister2);
    }

    public void initMethod(MethodEntry method, ClassEntry classEntry) {
        startText();
        comment("init Method");
        append(getLabel(method.getName(), classEntry.getName()) + ":");

        move("$fp", "$sp");
        push("$ra");

        // Pushea local vars
        for (VariableEntry var : method.getLocalVarList()) {
            var.initialize(this, getStackLocalVarOffset(method, var.getPosition()));
        }
    }

    public void initStart(MethodEntry method) {
        startText();
        comment("init Start");

        append("main:");

        move("$fp", "$sp");

        // Pushea local vars
        for (VariableEntry var : method.getLocalVarList()) {
            var.initialize(this, getStackLocalVarOffset(method, var.getPosition()));
        }
    }

    public void finishMethod() {
        pop("$ra");
        jumpRegister("$ra"); // Return to calling instruction
    }


    /**
     * @param method
     * @return el offset necesario para acceder al un parametro en el stack
     * desde el frame pointer
     */
    public int getStackParamOffset(int pararameterPosition, int totalParams) {
        return 4 * (totalParams - pararameterPosition);
    }

    /**
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
        offset -= (4 * varPosition);

        return offset;
    }

    public void addDataLabel(String name, String type, String value) {
        if (!name.equals("")) {
            name += ":";
        }
        appendTab(name + " " + type + " " + value);
    }

    public String getVirtualTableName(ClassEntry classEntry) {
        return "VT_" + classEntry.getName();
    }


    public void jumpAndLinkRegister(String register) {
        append("jalr " + register);
    }

    public void addDefaultValues() {
        startData();
        Map<String, String> defaultValues = AttributeType.getDefaultValues();
        for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
            if (entry.getKey().equals("Str") || entry.getKey().equals("Char")) {
                addDataLabel("defaultValue" + entry.getKey(), ".asciiz", entry.getValue());
            }
            else {
                addDataLabel("defaultValue" + entry.getKey(), ".word", entry.getValue());
            }
        }
    }

}
