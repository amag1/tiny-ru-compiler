package exceptions.semantic.symbolTable;

import lexical.Token;

public class TypeNotFoundException extends SymbolTableException {
    public TypeNotFoundException(Token token, String typeName) {
        super("Tipo no encontrado: " + typeName, token.getLocation());
    }
}
