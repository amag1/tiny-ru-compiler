package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.SymbolTable;

public class TinyRuAstHandler implements  AstHandler{

    private AbstractSyntaxTree ast;
    private SymbolTable st;

    public  void validateSenteces() throws SemanticException {
        for (AstClassEntry currentClass: ast.getClasses()) {
            currentClass.validateSentences();
        }
    }
}
