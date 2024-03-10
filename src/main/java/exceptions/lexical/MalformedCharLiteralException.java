package exceptions.lexical;

import location.Location;

public class MalformedCharLiteralException extends LexicalException{
    public MalformedCharLiteralException(String lexeme, Location location) {
        super("Caracter invalido: " + lexeme, location);
    }
}
