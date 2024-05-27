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
        sb.append("VT_IO:" + System.lineSeparator());
        sb.append(helper.comment("IO Virtual Table"));

        for (MethodEntry method : entry.getMethodList()) {
            sb.append(".word" + System.lineSeparator());
            sb.append(entry.getName() + "_" + method.getName() + ":" + System.lineSeparator());
            sb.append(helper.comment("IO method " + method));
            sb.append(helper.comment("Method not implemented"));
        }

        return sb.toString();
    }
}
