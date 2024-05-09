package exceptions.semantic.symbolTable;

import lexical.Token;

public class RedefinedInheritedAttributeException extends SymbolTableException {
    public RedefinedInheritedAttributeException(Token token) {
        super("Se redefinió un atributo heredado: " + token.getLexem(), token.getLocation());
    }
}
