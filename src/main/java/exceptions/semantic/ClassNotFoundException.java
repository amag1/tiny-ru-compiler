package exceptions.semantic;

import lexical.Token;

public class ClassNotFoundException extends SemanticException {
    public ClassNotFoundException(Token struct, String inherits) {
        super("Herencia inválida: la clase " + inherits + " no existe", struct.getLocation());
    }
}
