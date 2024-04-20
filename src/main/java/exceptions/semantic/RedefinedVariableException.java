package exceptions.semantic;

import lexical.Token;

public class RedefinedVariableException extends SemanticException {
    public RedefinedVariableException(Token token) {
        super("Variable redefinida: " + token.getLexem(), token.getLocation());
    }
}
