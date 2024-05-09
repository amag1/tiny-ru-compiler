package exceptions.semantic.symbolTable;

import lexical.Token;

public class ClassNotFoundException extends SymbolTableException {
    public ClassNotFoundException(Token struct, String inherits) {
        super("Herencia inválida: la clase " + inherits + " no existe", struct.getLocation());
    }
}
