package exceptions.semantic;

import lexical.Token;

public class OverridenMethodException extends SemanticException {
    public OverridenMethodException(Token token, String method) {
        super("Sobrecarga invalida para el metodo " + method, token.getLocation());
    }
}
