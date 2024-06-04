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

    public String generate(String reg1, String reg2) {
        return switch (operator.getLexem()) {
            case "+" -> "add " + "$a0" + ", " + reg1 + ", " + reg2;
            case "-" -> "sub " + "$a0" + ", " + reg1 + ", " + reg2;
            case "*" -> "mul " + "$a0" + ", " + reg1 + ", " + reg2;
            case "/" -> {
                String code = "beq " + reg2 + ", $zero, exception_division_by_zero\n";
                code += "div " + reg1 + ", " + reg2 + "\n";
                code += "mflo $a0";

                yield code;
            }

            case "%" -> {
                String code = "beq " + reg2 + ", $zero, exception_division_by_zero\n";
                code += "div " + reg1 + ", " + reg2 + "\n";
                code += "mfhi $a0";

                yield code;
            }

            case "==" -> {
                // Resta los dos valores
                String code = "sub $a0, " + reg1 + ", " + reg2 + "\n";
                // Normaliza el resultado
                code += "sltu $a0, $zero, $a0\n";

                // Convierte 0 en 1 y cualquier otro valor en 0
                code += "xori $a0, $a0, 1";

                yield code;
            }

            case "!=" -> {
                // Resta los dos valores
                String code = "sub $a0, " + reg1 + ", " + reg2 + "\n";
                // Normaliza el resultado
                code += "sltu $a0, $zero, $a0";

                yield code;
            }

            case ">" -> "slt $a0, " + reg2 + ", " + reg1;
            // Al invertir los registros, se invierte el resultado
            case "<" -> "slt $a0, " + reg1 + ", " + reg2;
            case ">=" -> {
                // Negacion de <
                String code = "slt $a0, " + reg1 + ", " + reg2 + "\n";
                code += "xori $a0, $a0, 1";
                yield code;
            }
            case "<=" -> {
                // Negacion de >
                String code = "slt $a0, " + reg2 + ", " + reg1 + "\n";
                code += "xori $a0, $a0, 1";
                yield code;
            }


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
