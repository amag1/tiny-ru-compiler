package codeGeneration.predefined;

import codeGeneration.Generable;
import codeGeneration.MipsHelper;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public class IOGenerator implements Generable {
    private MipsHelper helper;
    private ClassEntry entry;

    public IOGenerator(ClassEntry entry, MipsHelper helper) {
        this.helper = helper;
        this.entry = entry;
    }

    public String generate() {
        // Generar Virtual Table y codigo para cada metodo
        helper.append("VT_IO:");
        helper.comment("IO Virtual Table");

        for (MethodEntry method : entry.getMethodList()) {
            generateMethod(method);
        }

        return helper.getString();
    }

    private void generateMethod(MethodEntry method) {
        // Add method name
        helper.lineSeparator();
        helper.append(".data");
        helper.append(".word");
        helper.startText();

        helper.append(helper.getLabel(method, entry));
        helper.move("$fp", "$sp");

        helper.push("ra");
        switch (method.getName()) {
            case "out_str":
                generateOutStrMethod(method);
                break;
            case "out_int":
                generateOutIntMethod(method);
                break;
            case "in_int":
                generateInIntMethod(method);
                break;
            case "in_str":
                generateInStrMethod(method);
                break;
            case "in_bool":
                generateInBoolMethod(method);
                break;
            case "out_bool":
                generateOutBoolMethod( method);
                break;
            default:
                generateNotImplementedMethod( method);
                break;
        }

        helper.loadWord("$ra", "($fp)");
        helper.move("$fp", "$sp");
        helper.addIU("$sp", "$sp", 4);
        helper.jumpRegister("$ra");

        if (method.getName().equals("out_bool")) {
            // Generar mensajes de error al final
            helper.append( ".data");
            helper.append("bool_true_msg: .asciiz \"true\"");
            helper.append("bool_false_msg: .asciiz \"false\"");
        }
    }

    private void generateNotImplementedMethod( MethodEntry method) {
        helper.comment("IO method " + method.getName());
        helper.comment("Method not implemented");
    }

    private void generateOutStrMethod(MethodEntry method) {
        helper.comment("Pop argumento");
        helper.loadWord("$a0", "4($fp)");

        helper.comment("Print string");
        helper.load("$v0", 4);
        helper.appendTab("syscall");
    }

    private void generateOutBoolMethod(MethodEntry method) {
        helper.comment("Pop argumento");
        helper.loadWord("$t0", "4($fp)");

        helper.load("$v0", 4);
        helper.comment("Check if result is zero");
        helper.branchOnEqual("$t0", "$zero", "out_bool_false");
        helper.loadAddress("$a0", "bool_true_msg");
        helper.appendTab( "syscall");
        helper.jump("out_bool_end");

        helper.append( "out_bool_false:");
        helper.loadAddress("$a0", "bool_false_msg");
        helper.appendTab( "syscall");

        helper.append("out_bool_end:");
    }

    private void generateOutIntMethod( MethodEntry method) {
        helper.comment("Obtener primer argumento");
        helper.loadWord("$a0", "4($fp)");

        helper.comment("Print int");
        helper.load("$v0", 1);
        helper.appendTab( "syscall");

        helper.comment("Pop argumento");
    }

    private void generateInIntMethod(MethodEntry method) {
        helper.comment("Read int");
        helper.load("$v0", 5);
        helper.appendTab( "syscall");
        helper.comment("Store result in accumulator");
        helper.move("$a0", "$v0");
    }

    private void generateInBoolMethod(MethodEntry method) {
        helper.comment("Read int, then convert to zero or one");
        helper.load("$v0", 5);
        helper.appendTab( "syscall");
        helper.comment("Store result in intermediate register");
        helper.move("$t0", "$v0");

        helper.comment("Check if result is zero");
        helper.branchOnEqual("$t0", "$zero", "in_bool_false");
        helper.load("$a0", 1);
        helper.jump("in_bool_end");

        helper.append( "in_bool_false:");
        helper.load("$a0", 0);

        helper.append( "in_bool_end:");
    }

    private void generateInStrMethod(MethodEntry method) {
        helper.comment("Read string");
        helper.comment("Allocate space");
        helper.load("$v0", 9);

        helper.comment("Number of bytes to allocate");
        helper.load("$a0", 256);

        helper.appendTab( "syscall");

        helper.comment("Store result in accumulator");
        helper.move("$a0", "$v0");

        helper.comment("Read string");
        helper.load("$v0", 8);

        helper.comment("Max length of string");
        helper.load("$a1", 256);
        helper.appendTab( "syscall");
    }
}
