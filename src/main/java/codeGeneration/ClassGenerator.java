package codeGeneration;

import semantic.abstractSintaxTree.AstClassEntry;
import semantic.abstractSintaxTree.AstMethodEntry;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.ClassEntry;
import codeGeneration.predefined.IOGenerator;
import semantic.symbolTable.MethodEntry;

public class ClassGenerator implements Generable {
    private ClassEntry classEntry;
    private AstClassEntry astClassEntry;
    private Context context;

    private boolean debug;

    public ClassGenerator(Context context, ClassEntry classEntry, AstClassEntry astClassEntry, boolean debug) {
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
        }
        else {
            MethodEntry symbolTableConstructor = classEntry.getConstructor();
            AstMethodEntry astClassEntryConstructor = astClassEntry.getConstructor();
            sb.append(generateConstructor(symbolTableConstructor, astClassEntryConstructor));
        }

        // generar el constructor


        return sb.toString();
    }

    private String generatePredefinedCode() {
        switch (classEntry.getName()) {
            case "IO":
                return new IOGenerator(classEntry, debug).generate();
            case "Array":
                // TODO
                return "Array_length: \n";
            case "Str":
                // TODO
                return "Str_length: \nStr_concat: \n";
            default:
                return "";
        }
    }

    private String generateVirtualTable() {
        MipsHelper helper = new MipsHelper(debug);

        helper.startData();
        helper.append(helper.getVirtualTableName(classEntry) + ":");

        // Apendear nombre de métodos
        for (MethodEntry method : classEntry.getMethodList()) {
            String methodName = helper.getLabel(method.getName(), classEntry.getName());
            helper.addDataLabel("", ".word", methodName);
        }

        return helper.getString();
    }

    private String generateConstructor(MethodEntry symbolTableConstructor, AstMethodEntry astClassEntryConstructor) {
        MipsHelper helper = new MipsHelper(debug);

        helper.startText();
        helper.append(helper.getLabel("constructor", classEntry.getName()) + ":");

        // Generar codigo para las variables locales
        helper.initMethod(symbolTableConstructor, classEntry);

        // Generar codigo para las sentencias
        helper.append(astClassEntryConstructor.generate(context, debug));

        // El constructor guarda referencia a self
        helper.pop("$a0");
        helper.popLocalVariables(symbolTableConstructor);
        helper.finishMethod();

        return helper.getString();
    }
}
