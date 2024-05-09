package exceptions.semantic.syntaxTree;

import lexical.Token;

public class NonIntArrayIndexException extends AstException {
    public NonIntArrayIndexException(Token token) {
        super("Indice de arreglo no es de tipo entero: " + token.getLexem(), token.getLocation());
    }
}
