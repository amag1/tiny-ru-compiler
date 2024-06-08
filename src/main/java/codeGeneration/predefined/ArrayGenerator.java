package codeGeneration.predefined;

import codeGeneration.Generable;
import codeGeneration.MipsHelper;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public class ArrayGenerator implements Generable {
    private MipsHelper helper;
    private ClassEntry entry;

    public ArrayGenerator(ClassEntry entry, boolean debug) {
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

        switch (method.getName()) {
            case "length":
                generateLength();
                break;
        }

        helper.finishMethod();

    }

    public void generateLength() {
        helper.comment("Start length");
        // Acceder a self
        helper.loadWord("$a0", "4($fp)");
        // Acceder a la longitud
        helper.loadWord("$a0", "4($a0)");
    }

}
