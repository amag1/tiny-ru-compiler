package exceptions.semantic.syntaxTree;

import lexical.Token;

public class InvalidStaticMethodCallException extends AstException {
    public InvalidStaticMethodCallException(Token method) {
        super("El metodo " + method.getLexem() + " no es estatico", method.getLocation());
    }
}
