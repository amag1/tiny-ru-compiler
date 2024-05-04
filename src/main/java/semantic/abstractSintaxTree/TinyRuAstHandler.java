package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.ChainNode;
import semantic.abstractSintaxTree.Expression.LiteralNode;
import semantic.abstractSintaxTree.Expression.PrimaryNode;
import semantic.abstractSintaxTree.Expression.VariableAccessNode;
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

    public VariableAccessNode createVariableAccess(Token token) {
        return new VariableAccessNode(token.getLexem());
    }

    public PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode) {
        if (childrenNode == null) { return parentNode;}
        else { return new ChainNode(parentNode, childrenNode);}
    }

}
