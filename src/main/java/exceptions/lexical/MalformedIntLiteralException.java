package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un literal entero mal formado.
 */
public class MalformedIntLiteralException extends LexicalException {
    public MalformedIntLiteralException(String lexeme, Location location) {
        super("Literal entero mal formado: " + lexeme, location);
    }
}
