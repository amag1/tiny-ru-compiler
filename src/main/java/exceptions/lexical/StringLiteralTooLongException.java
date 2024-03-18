package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un literal string que supera los 1024 caracteres.
 */
public class StringLiteralTooLongException extends LexicalException {
    public StringLiteralTooLongException(String lexeme, Location location) {
        super("Literal string supera 1024 caracteres: " + lexeme.substring(0, 8) + "...", location);
    }
}
