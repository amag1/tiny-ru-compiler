package codeGeneration;

import semantic.abstractSintaxTree.AstClassEntry;
import semantic.symbolTable.ClassEntry;
import codeGeneration.predefined.IOGenerator;

public class ClassGenerator implements Generable {
    private ClassEntry classEntry;
    private AstClassEntry astClassEntry;

    private MipsHelper helper;


    private boolean debug;

    public ClassGenerator(ClassEntry classEntry, AstClassEntry astClassEntry, MipsHelper helper) {
        this.classEntry = classEntry;
        this.astClassEntry = astClassEntry;
        this.helper = helper;
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
                return new IOGenerator(classEntry, helper).generate();
            default:
                return "";
        }
    }


}
