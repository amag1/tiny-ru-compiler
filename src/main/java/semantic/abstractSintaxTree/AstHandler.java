package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;

public interface AstHandler {
    public void validateSenteces() throws SemanticException;

    LiteralNode createLiteral(Token token);

    VariableAccessNode createVariableAccess(Token token);

    ArrayAccessNode createArrayAccess(Token token);

    PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode);

    MethodCallNode createMethodCallNode(Token methodToken);

    ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression);

}
