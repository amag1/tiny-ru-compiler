package exceptions.semantic.syntaxTree;

import lexical.Token;

public class UnexpectedReturnException extends AstException{
    public UnexpectedReturnException(String methodName, Token token) {
        super("Retorno inesperado en el método " + methodName, token.getLocation());
    }
}
