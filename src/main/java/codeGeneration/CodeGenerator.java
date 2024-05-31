package codeGeneration;

import semantic.abstractSintaxTree.*;
import semantic.symbolTable.*;

import java.util.Map;

public class CodeGenerator {
    private AbstractSyntaxTree ast;
    private SymbolTable symbolTable;
    private Boolean debug = true;


    public CodeGenerator(AbstractSyntaxTree ast, SymbolTable symbolTable) {
        this.ast = ast;
        this.symbolTable = symbolTable;
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();

        // Generate Macros
        MipsHelper macrosHelper = new MipsHelper(debug);
        macrosHelper.generateMacros();
        sb.append(macrosHelper.getString());

        // Generar codigo para start
        Context context = new Context(symbolTable);
        sb.append(generateStart(context));

        // Generar codigo pra las clases
        for (ClassEntry classEntry : symbolTable.getClasses()) {
            // Obtener clase del ast
            AstClassEntry astClassEntry = ast.getClasses().get(classEntry.getName());
            ClassGenerator classGenerator = new ClassGenerator(context, classEntry, astClassEntry, debug);
            sb.append(classGenerator.generate());
        }

        return sb.toString();
    }


    public String generateStart(Context startContext) {
        MipsHelper startHelper = new MipsHelper(debug);

        // Genera código del start
        AstMethodEntry start = ast.getStart();
        MethodEntry startMethod = symbolTable.getStart();

        // Generar codigo para las variables locales
        // Actualiza el frame pointer
        startHelper.initStart(startMethod);
        startHelper.append(start.generate(startContext, debug));

        // Popear todas las variables locales
        startHelper.popLocalVariables(startMethod);

        // Finaliza el programa
        startHelper.syscall(10);

        startHelper.addDefaultValues();

        return startHelper.getString();
    }
}
