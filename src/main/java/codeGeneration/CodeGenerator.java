package codeGeneration;

import semantic.abstractSintaxTree.*;
import semantic.symbolTable.*;

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
        sb.append(generateStart());

        // Generar codigo pra las clases
        for (ClassEntry classEntry : symbolTable.getClasses()) {
            // Obtener clase del ast
            AstClassEntry astClassEntry = ast.getClasses().get(classEntry.getName());
            ClassGenerator classGenerator = new ClassGenerator(classEntry, astClassEntry, debug);
            sb.append(classGenerator.generate());
        }

        return sb.toString();
    }


    public String generateStart() {
        MipsHelper startHelper = new MipsHelper(debug);
        startHelper.comment("start program");
        startHelper.startText();
        startHelper.append("main:");

        // Setea el frame pointer al inicio del stack
        startHelper.move("$fp", "$sp");

        // Genera código del start
        AstMethodEntry start = ast.getStart();
        Context startContext = new Context(symbolTable);
        startHelper.append(start.generate(startContext, debug));


        // Finaliza el programa
        startHelper.syscall(10);

        return startHelper.getString();
    }
}
