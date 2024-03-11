package exceptions.lexical;

import location.Location;

public class UnclosedCharLiteralException extends LexicalException {
    public UnclosedCharLiteralException(String lexeme, Location location) {
        super("Caracter no cerrado: " + lexeme, location);
    }
}
