package exceptions.semantic.symbolTable;

import lexical.Token;

public class StaticMethodOverridenException extends SymbolTableException {
    public StaticMethodOverridenException(Token token, String method) {
        super("Redefinción de un método estático: " + method, token.getLocation());
    }
}
