package exceptions.semantic.syntaxTree;

import lexical.Token;

public class ParameterTypeMismatchException extends AstException {
    public ParameterTypeMismatchException(String expected, String received, Token token) {
        super("Se esperaba una expresion de tipo " + expected + " pero se recibio una expresion de tipo " + received, token.getLocation());
    }
}
