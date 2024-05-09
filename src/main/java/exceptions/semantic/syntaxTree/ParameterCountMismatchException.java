package exceptions.semantic.syntaxTree;

import lexical.Token;

public class ParameterCountMismatchException extends AstException {
    public ParameterCountMismatchException(String functionName, int expected, int received, Token token) {
        super("Metodo " + functionName + " esperaba " + expected + " parametros, pero recibio " + received + " parametros.", token.getLocation());
    }

}
