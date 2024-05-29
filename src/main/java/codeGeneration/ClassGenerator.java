package codeGeneration;

import semantic.abstractSintaxTree.AstClassEntry;
import semantic.symbolTable.ClassEntry;
import codeGeneration.predefined.IOGenerator;

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

        if (classEntry.isPredefined()) {
            sb.append(generatePredefinedCode());
        }

        return sb.toString();
    }

    private String generatePredefinedCode() {
        switch (classEntry.getName()) {
            case "IO":
                return new IOGenerator(classEntry, debug).generate();
            default:
                return "";
        }
    }


}
