package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra una cadena no cerrada.
 */
public class UnclosedStringLiteralException extends LexicalException {
    public UnclosedStringLiteralException(String lexeme, Location location) {
        super("Cadena no cerrada: " + lexeme, location);
    }
}
