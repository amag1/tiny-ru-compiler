package exceptions.semantic.symbolTable;

import lexical.Token;

public class RedefinedStructException extends SymbolTableException {
    public RedefinedStructException(Token token) {
        super("Struct redefinida: " + token.getLexem(), token.getLocation());
    }
}
