package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un caracter inválido.
 */
public class InvalidCharacterException extends LexicalException {
    public InvalidCharacterException(Character c, Location location) {
        super("Caracter invalido: " + c, location);
    }
}
