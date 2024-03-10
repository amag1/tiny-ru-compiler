package exceptions.lexical;

import location.Location;

public class UnclosedStringLiteralException extends LexicalException{
    public UnclosedStringLiteralException(String lexeme, Location location) {
        super("Cadena no cerrada: " + lexeme, location);
    }
}
