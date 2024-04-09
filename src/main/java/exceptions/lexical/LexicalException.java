package exceptions.lexical;

import exceptions.TinyRuException;
import location.Location;

/**
 * Excepción lanzada cuando se encuentra un error léxico.
 * Esta excepción contiene la ubicación del error.
 * Las demás excepciones de errores léxicos heredan de esta clase.
 */
public class LexicalException extends TinyRuException {
    public LexicalException(String message, Location location) {
        super(message, location);
    }
}
