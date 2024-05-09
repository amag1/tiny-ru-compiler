package semantic.abstractSintaxTree.Expression;

import lexical.Token;
import semantic.Json;

public class Operator  implements Json {
    Token operator;
    String type;

    public Operator(Token operator) {
        this.operator = operator;
    }

    @Override
    public String toJson(int indentationIndex) {
        return "\"" + operator.getLexem() +  "\"";
    }
}
