package exceptions.lexical;

import location.Location;

public class MalformedClassIdentifierException extends LexicalException{
    public MalformedClassIdentifierException(String lexeme, Location location) {
        super("Identificador de clase mal formado: " + lexeme, location);
    }
}
