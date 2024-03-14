package exceptions.lexical;

import location.Location;
public class MalformedDoubleSymbolException extends LexicalException{
    public MalformedDoubleSymbolException(char lexeme, Location location) {
        super("Se esperaba simbolo doble: " + lexeme, location);
    }
}
