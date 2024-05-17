package exceptions.semantic.syntaxTree;

import lexical.Token;

public class NonIntArrayLengthException extends AstException {
    public NonIntArrayLengthException(Token token) {
        super("Longitud de arreglo no es de tipo entero" , token.getLocation());
    }
}
