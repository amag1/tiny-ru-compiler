package exceptions.syntactic;

import exceptions.TinyRuException;
import lexical.Token;
import lexical.Type;

import java.util.Arrays;

public class SyntacticException extends TinyRuException {
    public SyntacticException(Token token, Type... expected) {
        super("Se esperaba " + Arrays.toString(expected) + " pero se encontró " + token.getType(), token.getLocation());
    }

    public SyntacticException(Token token, String expected) {
        super("Se esperaba " + expected + " pero se encontró " + token.getType(), token.getLocation());
    }
}
