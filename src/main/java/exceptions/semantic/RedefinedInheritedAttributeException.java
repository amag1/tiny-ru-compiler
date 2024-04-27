package exceptions.semantic;

import lexical.Token;

public class RedefinedInheritedAttributeException extends SemanticException {
    public RedefinedInheritedAttributeException(Token token) {
        super("Se redefinió un atributo heredado: " + token.getLexem(), token.getLocation());
    }
}
