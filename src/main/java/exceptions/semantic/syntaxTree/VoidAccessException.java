package exceptions.semantic.syntaxTree;

import lexical.Token;

public class VoidAccessException extends AstException {
    public VoidAccessException(Token token) {
        super("Error: no se puede acceder a una expresión de tipo void", token.getLocation());
    }
}
