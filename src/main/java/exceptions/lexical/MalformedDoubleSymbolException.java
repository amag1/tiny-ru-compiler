package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un simbolo doble mal formado.
 */
public class MalformedDoubleSymbolException extends LexicalException {
    public MalformedDoubleSymbolException(char lexeme, Location location) {
        super("Se esperaba simbolo doble: " + lexeme, location);
    }
}
