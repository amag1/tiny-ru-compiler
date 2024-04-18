package exceptions.semantic;

import lexical.Token;

public class RedefinedStructException extends SemanticException {
    public RedefinedStructException(Token token) {
        super("Struct redefinida: " + token.getLexem(), token.getLocation());
    }
}
