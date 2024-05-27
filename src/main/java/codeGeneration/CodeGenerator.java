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
    private MipsHelper helper;

    public CodeGenerator(AbstractSyntaxTree ast, SymbolTable symbolTable) {
        this.ast = ast;
        this.symbolTable = symbolTable;
        this.helper = new MipsHelper(debug);
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(helper.generateMacros());

        for (ClassEntry classEntry : symbolTable.getClasses()) {
            // Obtener clase del ast
            AstClassEntry astClassEntry = ast.getClasses().get(classEntry.getName());
            ClassGenerator classGenerator = new ClassGenerator(classEntry, astClassEntry, helper);
            sb.append(classGenerator.generate());
        }

        return sb.toString();
    }


}
