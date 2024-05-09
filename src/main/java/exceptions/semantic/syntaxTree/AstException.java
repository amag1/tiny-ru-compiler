package exceptions.semantic.syntaxTree;

import exceptions.TinyRuException;
import location.Location;

public class AstException extends TinyRuException {
    public AstException(String message, Location location) {
        super(message, location);
    }
}
