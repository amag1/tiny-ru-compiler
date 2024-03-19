package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un identificador de clase mal formado.
 */
public class MalformedClassIdentifierException extends LexicalException {
    public MalformedClassIdentifierException(String lexeme, Location location) {
        super("Identificador de clase mal formado: " + lexeme, location);
    }
}
