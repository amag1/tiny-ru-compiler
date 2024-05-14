package exceptions.semantic.syntaxTree;

import lexical.Token;

public class InvalidMethodReturn extends AstException{
    public InvalidMethodReturn(String expectedType, String foundType, Token token) {
        super("Método retorna tipo " + expectedType + " pero se encontŕo " + foundType , token.getLocation());
    }
}
