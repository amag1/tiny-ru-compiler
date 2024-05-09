package exceptions.semantic.syntaxTree;

import lexical.Token;

public class UnaryTypeMismatchException extends AstException {
    public UnaryTypeMismatchException(Token token, String expected, String found) {
        super("Error de tipo en operacion unaria: se esperaba expresion de tipo " +
                expected + " pero se encontro expresion de tipo " + found, token.getLocation());
    }
}
