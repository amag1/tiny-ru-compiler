package codeGeneration;

import semantic.abstractSintaxTree.AstClassEntry;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.ClassEntry;
import codeGeneration.predefined.IOGenerator;
import semantic.symbolTable.MethodEntry;

public class ClassGenerator implements Generable {
    private ClassEntry classEntry;
    private AstClassEntry astClassEntry;

    private boolean debug;

    public ClassGenerator(ClassEntry classEntry, AstClassEntry astClassEntry, boolean debug) {
        this.classEntry = classEntry;
        this.astClassEntry = astClassEntry;
        this.debug = debug;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();

        // Generar virtual tables
        sb.append(generateVirtualTable());

        if (classEntry.isPredefined()) {
            sb.append(generatePredefinedCode());
        } else {
            // TODO
        }

        return sb.toString();
    }

        private String generatePredefinedCode() {
        switch (classEntry.getName()) {
            case "IO":
                return new IOGenerator(classEntry, debug).generate();
            case "Array":
                // TODO
            case "String":
                // TODO
            default:
                return "";
        }
    }

    private String generateVirtualTable() {
        MipsHelper helper = new MipsHelper(debug);

        helper.startData();
        helper.append(helper.getVirtualTableName(classEntry) + ":");

        // Apendear nombre de métodos
        for (MethodEntry method: classEntry.getMethodList()) {
            String methodName = helper.getLabel(method.getName(),classEntry.getName());
            helper.addDataLabel("", ".word", methodName);
        }

        return helper.getString();
    }
}
