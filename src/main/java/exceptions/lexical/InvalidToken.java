package exceptions.lexical;

import location.Location;

public class InvalidToken extends LexicalException{
    public InvalidToken(Location location) {
        super("Token invalido: ", location);
    }
}
