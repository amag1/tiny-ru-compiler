package exceptions.semantic.symbolTable;

import lexical.Token;

public class OverridenMethodException extends SymbolTableException {
    public OverridenMethodException(Token token, String method) {
        super("Sobrecarga invalida para el metodo " + method, token.getLocation());
    }
}
