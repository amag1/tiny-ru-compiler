package exceptions.lexical;

import location.Location;
public class MalformedIdentifierException extends LexicalException{
    public MalformedIdentifierException(String lexeme, Location location) {
        super("Identificador mal formado: " + lexeme, location);
    }
}
