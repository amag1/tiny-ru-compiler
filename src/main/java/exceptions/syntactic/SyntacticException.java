package exceptions.syntactic;

import exceptions.TinyRuException;
import lexical.Token;
import lexical.Type;
import location.Location;

import java.util.Arrays;

public class SyntacticException extends TinyRuException {
    public SyntacticException(String message, Location location) {
        super(message, location);
    }

    public SyntacticException(Token token, Type... expected) {
        super("Se esperaba " + Arrays.toString(expected) + " pero se encontró " + token.getType(), token.getLocation());
    }

}
