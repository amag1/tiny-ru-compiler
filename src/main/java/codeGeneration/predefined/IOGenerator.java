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
        helper.appendTab("move $fp, $sp");
        helper.appendTab("push($ra)");
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

        helper.appendTab( "lw $ra, ($fp)");
        helper.appendTab( "move $fp, $sp");
        helper.appendTab( "addiu $sp, $sp, 4");
        helper.appendTab( "jr $ra");

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
        helper.appendTab("lw $a0, 4($fp)");

        helper.comment("Print string");
        helper.appendTab("li $v0, 4");
        helper.appendTab("syscall");
    }

    private void generateOutBoolMethod(MethodEntry method) {
        helper.comment("Pop argumento");
        helper.appendTab( "lw $t0, 4($fp)");

        helper.appendTab( "li $v0, 4");
        helper.comment("Check if result is zero");
        helper.appendTab( "beq $t0, $zero, out_bool_false");
        helper.appendTab( "la $a0, bool_true_msg");
        helper.appendTab( "syscall");
        helper.appendTab( "j out_bool_end");

        helper.append( "out_bool_false:");
        helper.appendTab( "la $a0, bool_false_msg");
        helper.appendTab( "syscall");

        helper.append( "out_bool_end:");
    }

    private void generateOutIntMethod( MethodEntry method) {
        helper.comment("Obtener primer argumento");
        helper.appendTab( "lw $a0, 4($fp)");

        helper.comment("Print int");
        helper.appendTab( "li $v0, 1");
        helper.appendTab( "syscall");

        helper.comment("Pop argumento");
    }

    private void generateInIntMethod(MethodEntry method) {
        helper.comment("Read int");
        helper.appendTab( "li $v0, 5");
        helper.appendTab( "syscall");
        helper.comment("Store result in accumulator");
        helper.appendTab( "move $a0, $v0");
    }

    private void generateInBoolMethod(MethodEntry method) {
        helper.comment("Read int, then convert to zero or one");
        helper.appendTab( "li $v0, 5");
        helper.appendTab( "syscall");
        helper.comment("Store result in intermediate register");
        helper.appendTab( "move $t0, $v0");

        helper.comment("Check if result is zero");
        helper.appendTab( "beq $t0, $zero, in_bool_false");
        helper.appendTab( "li $a0, 1");
        helper.appendTab( "j in_bool_end");

        helper.append( "in_bool_false:");
        helper.appendTab( "li $a0, 0");

        helper.append( "in_bool_end:");
    }

    private void generateInStrMethod(MethodEntry method) {
        helper.comment("Read string");
        helper.comment("Allocate space");
        helper.appendTab( "li $v0, 9");

        helper.comment("Number of bytes to allocate");
        helper.appendTab( "li $a0, 256");

        helper.appendTab( "syscall");

        helper.comment("Store result in accumulator");
        helper.appendTab( "move $a0, $v0");

        helper.comment("Read string");
        helper.appendTab( "li $v0, 8");

        helper.comment("Max length of string");
        helper.appendTab( "li $a1, 256");
        helper.appendTab( "syscall");
    }
}
