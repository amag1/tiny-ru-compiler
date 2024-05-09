package exceptions.semantic.syntaxTree;

import lexical.Token;

public class VariableIsNotArrayException extends AstException {
    public VariableIsNotArrayException(Token token) {
        super("Variable " + token.getLexem() + " no es un arreglo", token.getLocation());
    }
}
