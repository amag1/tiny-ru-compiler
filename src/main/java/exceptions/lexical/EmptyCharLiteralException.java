package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un literal caracter vacio.
 */
public class EmptyCharLiteralException extends LexicalException {
    public EmptyCharLiteralException(Location location) {
        super("Caracter vacio", location);
    }
}
