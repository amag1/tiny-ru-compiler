package exceptions.semantic.syntaxTree;

import lexical.Token;

public class UndeclaredVariableAccessException extends AstException {
    public UndeclaredVariableAccessException(Token token) {
        super("Variable no declarada: " + token.getLexem(), token.getLocation());
    }
}
