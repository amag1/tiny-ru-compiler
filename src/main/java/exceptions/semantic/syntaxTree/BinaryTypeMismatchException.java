package exceptions.semantic.syntaxTree;

import lexical.Token;

public class BinaryTypeMismatchException extends AstException {
    public BinaryTypeMismatchException(Token token, String expected, String found) {
        super("Error de tipo en operacion binaria: se esperaba expresion de tipo " +
                expected + " pero se encontro expresion de tipo " + found, token.getLocation());
    }
}
