package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.ArrayAccessNode;
import semantic.abstractSintaxTree.Expression.LiteralNode;
import semantic.abstractSintaxTree.Expression.PrimaryNode;
import semantic.abstractSintaxTree.Expression.VariableAccessNode;

public interface AstHandler {
    public void validateSenteces() throws SemanticException;

    LiteralNode createLiteral(Token token);

    VariableAccessNode createVariableAccess(Token token);

    ArrayAccessNode createArrayAccess(Token token);

    PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode);
}
