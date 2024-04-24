package exceptions.semantic;

import exceptions.TinyRuException;
import location.Location;

public class SemanticException extends TinyRuException {
    public SemanticException(String message, Location location) {
        super(message, location);
    }
}
