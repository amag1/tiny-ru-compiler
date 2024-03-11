package exceptions.lexical;

import location.Location;

public class MalformedStringLiteralException extends LexicalException{
    public MalformedStringLiteralException(String lexeme, Location location) {
        super("Cadena mal formada: " + lexeme, location);
    }
}
