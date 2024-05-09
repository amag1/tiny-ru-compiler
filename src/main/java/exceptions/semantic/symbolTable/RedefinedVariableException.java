package exceptions.semantic.symbolTable;

import lexical.Token;

public class RedefinedVariableException extends SymbolTableException {
    public RedefinedVariableException(Token token) {
        super("Variable redefinida: " + token.getLexem(), token.getLocation());
    }
}
