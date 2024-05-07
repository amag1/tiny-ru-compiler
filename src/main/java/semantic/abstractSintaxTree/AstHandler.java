package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import syntactic.ConstructorCallNode;

import java.util.List;

public interface AstHandler {
    void validateSenteces() throws SemanticException;

    LiteralNode createLiteral(Token token);

    VariableAccessNode createVariableAccess(Token token);

    ArrayAccessNode createArrayAccess(Token token, ExpressionNode indexExpression);

    PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode);

    MethodCallNode createMethodCallNode(Token methodToken);

    ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression);

    ConstructorCallNode createConstructorCallNode(Token classToken);

    NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression);

    StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken);

    SelfAccess createSelfAccess(PrimaryNode node);

    void SetMethodParameter(MethodCall method, List<ExpressionNode> parameters);
}
