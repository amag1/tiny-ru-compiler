package exceptions.semantic.syntaxTree;

import lexical.Token;

public class ParameterTypeMismatchException extends AstException {
    public ParameterTypeMismatchException(String functionName, String expected, String received, Token token) {
        super("Metodo " + functionName + " esperaba un parametro de tipo " + expected + " pero recibio un parametro de tipo " + received, token.getLocation());
    }
}
