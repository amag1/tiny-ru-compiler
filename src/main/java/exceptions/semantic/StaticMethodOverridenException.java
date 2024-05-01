package exceptions.semantic;

import lexical.Token;

public class StaticMethodOverridenException extends SemanticException {
    public StaticMethodOverridenException(Token token, String method) {
        super("Redefinción de un método estático: " + method, token.getLocation());
    }
}
