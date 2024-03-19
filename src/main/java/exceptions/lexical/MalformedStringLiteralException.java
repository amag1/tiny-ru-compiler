package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un literal string mal formado.
 */
public class MalformedStringLiteralException extends LexicalException {
    public MalformedStringLiteralException(String lexeme, Location location) {
        super("Cadena mal formada: " + lexeme, location);
    }
}
