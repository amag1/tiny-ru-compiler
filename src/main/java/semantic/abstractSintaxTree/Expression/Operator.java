package semantic.abstractSintaxTree.Expression;

import lexical.Token;
import semantic.Json;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;

public class Operator implements Json {
    Token operator;

    public Operator(Token operator) {
        this.operator = operator;
    }

    public AttributeType getAttributeType() {
        return switch (operator.getLexem()) {
            case "+", "-", "*", "/", "%", "++", "--" ->
                    new AttributeType("Int");
            // case ">", "<", ">=", "<=", "==", "!=", "&&", "||", "!" ->
            // new AttributeType("Bool");
            default -> new AttributeType("Bool");
        };
    }

    public AttributeType getInputType() {
        return switch (operator.getLexem()) {
            case "+", "-", "*", "/", "%", "++", "--", ">", "<", ">=", "<=" ->
                    new AttributeType("Int");
            case "==", "!=" -> new AttributeType("Any");
            // case "&&", "||", "!" -> new AttributeType("Bool");
            default -> new AttributeType("Bool");
        };
    }

    public Token getToken() {
        return operator;
    }

    @Override
    public String toJson(int indentationIndex) {
        return "\"" + operator.getLexem() + "\"";
    }
}
