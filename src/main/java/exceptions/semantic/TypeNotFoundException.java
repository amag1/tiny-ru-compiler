package exceptions.semantic;

import lexical.Token;

public class TypeNotFoundException extends SemanticException {
    public TypeNotFoundException(Token token, String typeName) {
        super("Tipo no encontrado: " + typeName, token.getLocation());
    }
}
