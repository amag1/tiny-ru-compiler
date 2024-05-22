package exceptions.semantic.syntaxTree;

import lexical.Token;

public class OnlyVarException extends AstException {
    public OnlyVarException(Token token) {
        super("Se esperaba una variable", token.getLocation());
    }
}
