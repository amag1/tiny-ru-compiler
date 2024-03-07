package exceptions.lexical;

import location.Location;

public class MalformedClassIdentifierException extends LexicalException{
    public MalformedClassIdentifierException(Location location) {
        super("Identificador de clase mal formado", location);
    }
}
