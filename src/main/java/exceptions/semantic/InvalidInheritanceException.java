package exceptions.semantic;

import lexical.Token;

public class InvalidInheritanceException extends SemanticException {
    public InvalidInheritanceException(Token token) {
        super("No se puede heredar de tipo: " + token.getLexem(), token.getLocation());
    }
}
