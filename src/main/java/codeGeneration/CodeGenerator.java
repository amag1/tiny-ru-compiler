package codeGeneration;

import semantic.abstractSintaxTree.AbstractSyntaxTree;
import semantic.abstractSintaxTree.AstClassEntry;
import semantic.abstractSintaxTree.AstHandler;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.SymbolTable;
import semantic.symbolTable.SymbolTableHandler;
import semantic.symbolTable.SymbolTableLookup;

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

        for (ClassEntry classEntry : symbolTable.getClasses()) {
            // Obtener clase del ast
            AstClassEntry astClassEntry = ast.getClasses().get(classEntry.getName());
            ClassGenerator classGenerator = new ClassGenerator(classEntry, astClassEntry, debug);
            sb.append(classGenerator.generate());
        }

        return sb.toString();
    }


}
