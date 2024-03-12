package exceptions.lexical;

import location.Location;

public class EmptyCharLiteralException extends LexicalException{
    public EmptyCharLiteralException(Location location) {
        super("Caracter vacio", location);
    }
}
