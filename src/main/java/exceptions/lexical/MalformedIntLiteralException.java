package exceptions.lexical;

import location.Location;

public class MalformedIntLiteralException extends LexicalException{
    public MalformedIntLiteralException(String lexeme, Location location) {
        super("Identificador de entero mal formado: " + lexeme, location);
    }
}
