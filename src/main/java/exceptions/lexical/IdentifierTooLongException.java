package exceptions.lexical;

import location.Location;

public class IdentifierTooLongException extends LexicalException {
    public IdentifierTooLongException(String lexeme, Location location) {
        super("Identificador excede 1024 caracteres: " + lexeme, location);
    }
}
