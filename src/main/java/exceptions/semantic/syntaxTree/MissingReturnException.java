package exceptions.semantic.syntaxTree;

import lexical.Token;

public class MissingReturnException extends AstException {
    public MissingReturnException(String method, Token token) {
        super("No se encontro return en metodo " + method, token.getLocation());
    }
}
