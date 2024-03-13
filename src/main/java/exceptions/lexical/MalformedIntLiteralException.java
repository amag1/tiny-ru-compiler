package exceptions.lexical;

import location.Location;

public class MalformedIntLiteralException extends LexicalException{
    public MalformedIntLiteralException(String lexeme, Location location) {
        super("Literal entero mal formado: " + lexeme, location);
    }
}
