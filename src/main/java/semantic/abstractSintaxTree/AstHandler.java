package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.LiteralNode;
import semantic.abstractSintaxTree.Expression.VariableAccessNode;

public interface AstHandler {
    public  void validateSenteces() throws SemanticException;

    public LiteralNode createLiteral(Token token);

    public VariableAccessNode createVariableAccess(Token token);
}
