package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.symbolTable.SymbolTable;
import semantic.abstractSintaxTree.Expression.ConstructorCallNode;

import java.util.List;

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
        return new VariableAccessNode(token.getLexem());
    }

    @Override
    public ArrayAccessNode createArrayAccess(Token token, ExpressionNode indexExpression) {
        return new ArrayAccessNode(token, indexExpression);
    }

    public PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode) {
        if (childrenNode == null) {
            return parentNode;
        }
        else {
            return new ChainNode(parentNode, childrenNode);
        }
    }

    public MethodCallNode createMethodCallNode(Token methodToken) {
        return new MethodCallNode(methodToken);
    }

    public ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression) {
        return new ParentizedExpressionNode(expression);
    }

    public ConstructorCallNode createConstructorCallNode(Token classToken) {
        return new ConstructorCallNode(classToken.getLexem());
    }

    public NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        return new NewArrayNode(elementsTypeToken, lengthExpression);
    }

    public StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken) {
        return new StaticMethodCallNode(classToken.getLexem(), methodToken.getLexem());
    }

    public SelfAccess createSelfAccess(PrimaryNode node) {
        return new SelfAccess(node);
    }

    public void SetMethodParameter(MethodCall method, List<ExpressionNode> parameters) {
        method.setParameters(parameters);
    }
}
