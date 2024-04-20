package exceptions.semantic;

import exceptions.TinyRuException;
import lexical.Token;
import location.Location;

public class SemanticException extends TinyRuException {
    public SemanticException(String message, Location location) {
        super(message,  location);
    }
}
