package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.SymbolTable;

public class TinyRuAstHandler implements AstHandler {

    private AbstractSyntaxTree ast;
    private SymbolTable st;

    public void validateSenteces() throws SemanticException {
        for (AstClassEntry currentClass : ast.getClasses()) {
            currentClass.validateSentences();
        }
    }

    public LiteralNode createLiteral(Token token) {
        return new LiteralNode(token);
    }

    public VariableAccessNode createVariableAccess(Token token) {
        return new VariableAccessNode(token.getLexem(), false);
    }

    @Override
    public ArrayAccessNode createArrayAccess(Token token) {
        return new ArrayAccessNode(token);
    }

    public PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode) {
        if (childrenNode == null) {
            return parentNode;
        }
        else {
            return new ChainNode(parentNode, childrenNode);
        }
    }

    public  MethodCallNode createMethodCallNode(Token methodToken) {
        return  new MethodCallNode(methodToken);
    }

    public ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression) {
        return  new ParentizedExpressionNode(expression);
    }

}
