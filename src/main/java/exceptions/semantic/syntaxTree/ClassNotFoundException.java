package exceptions.semantic.syntaxTree;

import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;

public class ClassNotFoundException extends AstException {
    public ClassNotFoundException(Token token) {
        super("No se encontró la clase: " + token.getLexem(), token.getLocation());
    }
}
