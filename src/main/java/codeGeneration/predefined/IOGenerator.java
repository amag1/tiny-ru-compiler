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
        StringBuilder sb = new StringBuilder();
        helper.append(sb, "VT_IO:");
        helper.append(sb, helper.comment("IO Virtual Table"));

        for (MethodEntry method : entry.getMethodList()) {
            generateMethod(sb, method);
        }

        return sb.toString();
    }

    private void generateMethod(StringBuilder sb, MethodEntry method) {
        // Add method name
        helper.append(sb, System.lineSeparator());
        helper.append(sb, ".data");
        helper.append(sb, ".word");
        helper.append(sb, ".text");
        helper.append(sb, helper.getLabel(method, entry));
        helper.appendTab(sb, "move $fp, $sp");
        helper.appendTab(sb, "push($ra)");
        switch (method.getName()) {
            case "out_str":
                generateOutStrMethod(sb, method);
                break;
            case "out_int":
                generateOutIntMethod(sb, method);
                break;
            case "in_int":
                generateInIntMethod(sb, method);
                break;
            case "in_str":
                generateInStrMethod(sb, method);
                break;
            case "in_bool":
                generateInBoolMethod(sb, method);
                break;
            case "out_bool":
                generateOutBoolMethod(sb, method);
                break;
            default:
                generateNotImplementedMethod(sb, method);
                break;
        }

        helper.appendTab(sb, "lw $ra, ($fp)");
        helper.appendTab(sb, "move $fp, $sp");
        helper.appendTab(sb, "addiu $sp, $sp, 4");
        helper.appendTab(sb, "jr $ra");

        if (method.getName().equals("out_bool")) {
            // Generar mensajes de error al final
            helper.append(sb, ".data");
            helper.append(sb, "bool_true_msg: .asciiz \"true\"");
            helper.append(sb, "bool_false_msg: .asciiz \"false\"");
        }
    }

    private void generateNotImplementedMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("IO method " + method.getName()));
        helper.appendTab(sb, helper.comment("Method not implemented"));
    }

    private void generateOutStrMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("Pop argumento"));
        helper.appendTab(sb, "lw $a0, 4($fp)");

        helper.appendTab(sb, helper.comment("Print string"));
        helper.appendTab(sb, "li $v0, 4");
        helper.appendTab(sb, "syscall");
    }

    private void generateOutBoolMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("Pop argumento"));
        helper.appendTab(sb, "lw $t0, 4($fp)");

        helper.appendTab(sb, "li $v0, 4");
        helper.appendTab(sb, helper.comment("Check if result is zero"));
        helper.appendTab(sb, "beq $t0, $zero, out_bool_false");
        helper.appendTab(sb, "la $a0, bool_true_msg");
        helper.appendTab(sb, "syscall");
        helper.appendTab(sb, "j out_bool_end");

        helper.append(sb, "out_bool_false:");
        helper.appendTab(sb, "la $a0, bool_false_msg");
        helper.appendTab(sb, "syscall");

        helper.append(sb, "out_bool_end:");
    }

    private void generateOutIntMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("Obtener primer argumento"));
        helper.appendTab(sb, "lw $a0, 4($fp)");

        helper.appendTab(sb, helper.comment("Print int"));
        helper.appendTab(sb, "li $v0, 1");
        helper.appendTab(sb, "syscall");

        helper.appendTab(sb, helper.comment("Pop argumento"));
    }

    private void generateInIntMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("Read int"));
        helper.appendTab(sb, "li $v0, 5");
        helper.appendTab(sb, "syscall");
        helper.appendTab(sb, helper.comment("Store result in accumulator"));
        helper.appendTab(sb, "move $a0, $v0");
    }

    private void generateInBoolMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("Read int, then convert to zero or one"));
        helper.appendTab(sb, "li $v0, 5");
        helper.appendTab(sb, "syscall");
        helper.appendTab(sb, helper.comment("Store result in intermediate register"));
        helper.appendTab(sb, "move $t0, $v0");

        helper.appendTab(sb, helper.comment("Check if result is zero"));
        helper.appendTab(sb, "beq $t0, $zero, in_bool_false");
        helper.appendTab(sb, "li $a0, 1");
        helper.appendTab(sb, "j in_bool_end");

        helper.append(sb, "in_bool_false:");
        helper.appendTab(sb, "li $a0, 0");

        helper.append(sb, "in_bool_end:");
    }

    private void generateInStrMethod(StringBuilder sb, MethodEntry method) {
        helper.appendTab(sb, helper.comment("Read string"));
        helper.appendTab(sb, helper.comment("Allocate space"));
        helper.appendTab(sb, "li $v0, 9");

        helper.appendTab(sb, helper.comment("Number of bytes to allocate"));
        helper.appendTab(sb, "li $a0, 256");

        helper.appendTab(sb, "syscall");

        helper.appendTab(sb, helper.comment("Store result in accumulator"));
        helper.appendTab(sb, "move $a0, $v0");

        helper.appendTab(sb, helper.comment("Read string"));
        helper.appendTab(sb, "li $v0, 8");

        helper.appendTab(sb, helper.comment("Max length of string"));
        helper.appendTab(sb, "li $a1, 256");
        helper.appendTab(sb, "syscall");
    }
}
