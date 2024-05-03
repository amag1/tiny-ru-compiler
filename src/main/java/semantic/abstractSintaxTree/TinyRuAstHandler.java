package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.LiteralNode;
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

    public LiteralNode createLiteral(Token token) {
        return new LiteralNode(token);
    }

}
