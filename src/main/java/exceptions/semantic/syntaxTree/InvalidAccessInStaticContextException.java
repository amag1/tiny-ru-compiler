package exceptions.semantic.syntaxTree;

import lexical.Token;

public class InvalidAccessInStaticContextException extends AstException {
    public InvalidAccessInStaticContextException(Token variable) {
        super("No se puede referenciar a " + variable.getLexem() + " desde un contexto estático", variable.getLocation());
    }
}
