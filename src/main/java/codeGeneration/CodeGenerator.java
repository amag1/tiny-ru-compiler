package codeGeneration;

import semantic.abstractSintaxTree.AbstractSyntaxTree;
import semantic.abstractSintaxTree.AstClassEntry;
import semantic.abstractSintaxTree.AstHandler;
import semantic.abstractSintaxTree.AstMethodEntry;
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
        MipsHelper sh = new MipsHelper(debug);
        sh.comment("start program");
        sh.startText();
        sh.append("main:");

        // Setea el frame pointer al inicio del stack
        sh.move("$fp", "$sp");

        // Genera código del start
        AstMethodEntry start = ast.getStart();
        sb.append(start.generate());


        // Finaliza el programa
        sh.syscall(10);
        sb.append(sh.getString());

        // Generar codigo pra las clases
        for (ClassEntry classEntry : symbolTable.getClasses()) {
            // Obtener clase del ast
            AstClassEntry astClassEntry = ast.getClasses().get(classEntry.getName());
            ClassGenerator classGenerator = new ClassGenerator(classEntry, astClassEntry, debug);
            sb.append(classGenerator.generate());
        }

        return sb.toString();
    }


}
