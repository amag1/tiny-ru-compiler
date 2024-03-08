package exceptions.lexical;

import location.Location;

public class InvalidCharacterException extends LexicalException {
    public InvalidCharacterException(Character c, Location location) {
        super("Caracter invalido: " + c, location);
    }
}
