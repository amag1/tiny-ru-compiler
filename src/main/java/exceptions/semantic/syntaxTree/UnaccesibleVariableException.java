package exceptions.semantic.syntaxTree;

import lexical.Token;

public class UnaccesibleVariableException extends AstException {
    public UnaccesibleVariableException(Token token) {
        super("La variable `" + token.getLexem() + "` es privada en la superclase, y, por lo tanto, inaccesible", token.getLocation());
    }
}
