package codeGeneration.predefined;

import codeGeneration.Generable;
import codeGeneration.MipsHelper;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public class StrGenerator implements Generable {

    private MipsHelper helper;
    private ClassEntry entry;

    public StrGenerator(ClassEntry entry, boolean debug) {
        this.helper = new MipsHelper(debug);
        this.entry = entry;
    }


    public String generate() {
        // Generar codigo para cada metodo
        for (MethodEntry method : entry.getMethodList()) {
            generateMethod(method);
        }

        return helper.getString();
    }

    private void generateMethod(MethodEntry method) {
        // Add method name
        helper.lineSeparator();
        helper.initMethod(method, entry);

        switch (method.getName()){
            case "concat":
                generateConcat();
                break;
            case "length":
                generateLength();
                break;
        }

        helper.finishMethod();

    }


    public void generateConcat() {
        helper.comment("Start concat");

        // Usa t2 como string a modificar
        helper.loadWord("$t2", "8($fp)"); // Self (acceso a cir)
        helper.loadWord("$t2", "4($t2)"); // Acceso a valor

        // Usa t3 como array a appendear
        helper.loadWord("$t3", "4($fp)"); // Param (acceso a cir)
        helper.loadWord("$t3", "4($t3)"); // Accesso a valor

        // Recorrer primer string guardando en t0 el bit actual
        helper.comment("Iterate over first string until end");
        helper.append("concat_loop_1:");
        helper.append("lb $t0, 0($t2)"); // Cargar byte
        helper.branchOnEqual("$t0", "$zero", "end_concat_loop_1");
        helper.append("addi $t2, $t2, 1 "); // Mover un byte
        helper.jump("concat_loop_1");
        helper.append("end_concat_loop_1:");

        // Recorrer el segundo string guardando en t4 el bit actual
        // Guardar en las direcciones del primer string (address de t0) el bit
        helper.comment("Iterate over second string saving bytes");
        helper.append("concat_loop_2:");
        helper.append("lb $t4, 0($t3)");
        helper.branchOnEqual("$t4", "$zero","end_concat_loop_2");
        helper.append("sb $t4, 0($t2)"); // Save byte t4 en direccion de t0
        helper.append("addi $t2, $t2, 1");
        helper.append("addi $t3, $t3, 1");
        helper.jump("concat_loop_2 ");
        helper.append("end_concat_loop_2:");
    }

    public  void  generateLength() {
        helper.comment("Start length");

        // Usa t2 como string
        helper.loadWord("$t2", "4($fp)"); // Self (acceso a cir)
        helper.loadWord("$t2", "4($t2)"); // Acceso a valor

        // Recorrer el string guardando en a0 la longitud actual
        helper.loadAddress("$a0", "($zero)");
        helper.comment("Iterate over string until end");
        helper.append("str_length_loop:");
        helper.append("lb $t0, 0($t2)"); // Cargar byte
        helper.branchOnEqual("$t0", "$zero", "end_str_length_loop");
        helper.addIU("$a0", "$a0", 1);
        helper.append("addi $t2, $t2, 1 "); // Mover un byte
        helper.jump("str_length_loop");
        helper.append("end_str_length_loop:");
    }

}
