package semantic.abstractSintaxTree.Expression;

import lexical.Token;

public class Operator {
    Token operator;
    String type;

    public Operator(Token operator) {
        this.operator = operator;
    }
}
