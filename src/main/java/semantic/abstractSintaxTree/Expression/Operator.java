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

    /**
     * @return El tipo de dato que retorna la operación
     */
    public AttributeType getAttributeType() {
        return switch (operator.getLexem()) {
            case "+", "-", "*", "/", "%", "++", "--" ->
                    new AttributeType("Int");
            // case ">", "<", ">=", "<=", "==", "!=", "&&", "||", "!" ->
            // new AttributeType("Bool");
            default -> new AttributeType("Bool");
        };
    }

    /**
     * @return El tipo que espera como entrada la operación
     */
    public AttributeType getInputType() {
        return switch (operator.getLexem()) {
            case "+", "-", "*", "/", "%", "++", "--", ">", "<", ">=", "<=" ->
                    new AttributeType("Int");
            case "==", "!=" -> new AttributeType("Any");
            // case "&&", "||", "!" -> new AttributeType("Bool");
            default -> new AttributeType("Bool");
        };
    }

    // Toma un solo registro y genera el código de la operación
    // Debe llamarse con operaciones unarias
    // En todos los casos el resultado queda en el acumulador
    public String generate(String register) {
        return switch (operator.getLexem()) {
            case "++" -> "addi " + register + ", " + register + ", 1";
            case "--" -> "subi " + register + ", " + register + ", 1";
            case "+" -> "add " + register + ", " + register + ", 0";
            case "-" -> "sub " + register + ", $zero, " + register;
            case "!" -> "xori " + register + ", " + register + ", 1";
            default -> "nop";
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
