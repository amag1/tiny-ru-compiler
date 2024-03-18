package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un identificador mal formado.
 */
public class MalformedIdentifierException extends LexicalException {
    public MalformedIdentifierException(String lexeme, Location location) {
        super("Identificador mal formado: " + lexeme, location);
    }
}
