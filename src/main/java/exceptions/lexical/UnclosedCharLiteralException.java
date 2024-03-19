package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un literal caracter no cerrado.
 */
public class UnclosedCharLiteralException extends LexicalException {
    public UnclosedCharLiteralException(String lexeme, Location location) {
        super("Caracter no cerrado: " + lexeme, location);
    }
}
