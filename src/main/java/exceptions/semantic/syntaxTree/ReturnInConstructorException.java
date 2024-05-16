package exceptions.semantic.syntaxTree;

import lexical.Token;

public class ReturnInConstructorException extends AstException {
    public ReturnInConstructorException(Token token) {
        super("Return en constructor", token.getLocation());
    }
}
