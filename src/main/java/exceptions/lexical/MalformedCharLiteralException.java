package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un literal caracter mal formado.
 */
public class MalformedCharLiteralException extends LexicalException {
    public MalformedCharLiteralException(String lexeme, Location location) {
        super("Caracter invalido: " + lexeme, location);
    }
}
