package codeGeneration;

import codeGeneration.predefined.ArrayGenerator;
import codeGeneration.predefined.StrGenerator;
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
        this.context = context;
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
            AstMethodEntry astClassEntryConstructor = null;
            // generar el constructor
            if (astClassEntry != null) {
                astClassEntryConstructor = astClassEntry.getConstructor();
            }
            sb.append(generateConstructor(symbolTableConstructor, astClassEntryConstructor));

            // Generar codigo para los metodos
            for (MethodEntry method : classEntry.getMethodList()) {
                if (!method.getGeneratedIn().isEmpty()) {
                    continue;
                }
                AstMethodEntry astMethod = astClassEntry.getMethod(method.getName());
                sb.append(generateMethod(method, astMethod));
            }
        }


        return sb.toString();
    }

    private String generatePredefinedCode() {
        switch (classEntry.getName()) {
            case "IO":
                return new IOGenerator(classEntry, debug).generate();
            case "Array":
                // TODO
                return new ArrayGenerator(classEntry, debug).generate();
            case "Str":
                return new StrGenerator(classEntry, debug).generate();
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
            String className;
            if (!method.getGeneratedIn().isEmpty()) {
                className = method.getGeneratedIn();
            }
            else {
                className = classEntry.getName();
            }
            String methodName = helper.getLabel(method.getName(), className);
            helper.addDataLabel("", ".word", methodName);
        }

        return helper.getString();
    }

    private String generateConstructor(MethodEntry symbolTableConstructor, AstMethodEntry astClassEntryConstructor) {
        MipsHelper helper = new MipsHelper(debug);
        // Generar codigo para las variables locales
        helper.initMethod(symbolTableConstructor, classEntry);

        // Generar codigo para las sentencias
        if (astClassEntryConstructor != null) {
            helper.append(astClassEntryConstructor.generate(context, classEntry, symbolTableConstructor, debug));
        }

        // El constructor guarda referencia a self
        helper.pop("$a0");
        helper.popLocalVariables(symbolTableConstructor);
        helper.finishMethod();

        return helper.getString();
    }

    private String generateMethod(MethodEntry method, AstMethodEntry astMethod) {
        MipsHelper helper = new MipsHelper(debug);
        // Generar codigo para las variables locales
        helper.initMethod(method, classEntry);

        // Generar codigo para las sentencias
        if (astMethod != null) {
            helper.append(astMethod.generate(context, classEntry, method, debug));
        }

        // Agregar etiqueta de fin de metodo
        helper.append(helper.getEndLabel(method.getName(), classEntry.getName()) + ":");
        helper.popLocalVariables(method);
        helper.finishMethod();

        return helper.getString();
    }
}
