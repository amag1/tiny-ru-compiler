package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando un identificador excede 1024 caracteres.
 */
public class IdentifierTooLongException extends LexicalException {
    public IdentifierTooLongException(String lexeme, Location location) {
        super("Identificador excede 1024 caracteres: " + lexeme, location);
    }
}
