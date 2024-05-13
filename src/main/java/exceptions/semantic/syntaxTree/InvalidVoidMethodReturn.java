package exceptions.semantic.syntaxTree;

import lexical.Token;

public class InvalidVoidMethodReturn extends AstException{
    public InvalidVoidMethodReturn(Token token) {
        super("No se pude retornar un valor en un método void" , token.getLocation());
    }
}
