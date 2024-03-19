package exceptions.lexical;

import location.Location;

/**
 * Excepción lanzada cuando se encuentra un error léxico.
 * Esta excepción contiene la ubicación del error.
 * Las demás excepciones de errores léxicos heredan de esta clase.
 */
public class LexicalException extends Exception {
    private Location location;

    public LexicalException(String message, Location location) {

        super(message);
        this.location = location;
    }

    public int getLine() {
        return location.getLine();
    }

    public int getColumn() {
        return location.getColumn();
    }
}
