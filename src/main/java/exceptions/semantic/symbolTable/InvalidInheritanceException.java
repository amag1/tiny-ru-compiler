package exceptions.semantic.symbolTable;

import lexical.Token;

public class InvalidInheritanceException extends SymbolTableException {
    public InvalidInheritanceException(Token token) {
        super("No se puede heredar de tipo: " + token.getLexem(), token.getLocation());
    }
}
