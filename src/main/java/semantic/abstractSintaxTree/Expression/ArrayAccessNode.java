package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.*;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.VariableEntry;

public class ArrayAccessNode extends PrimaryNode {
    private String arrayName;
    /**
     * Expresion que representa el indice del array. Debe ser de tipo entero
     */
    private ExpressionNode index;

    private VariableEntry variable;

    public ArrayAccessNode(Token arrayName, ExpressionNode index) {
        this.nodeType = "arrayAccess";
        this.arrayName = arrayName.getLexem();
        this.index = index;
        this.token = arrayName;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Check que el array exista
        VariableEntry arr = context.getAttribute(this.arrayName);
        if (arr == null) {
            throw new UndeclaredVariableAccessException(this.getToken());
        }

        variable = arr;

        // Chequar si se puede acceder al atributo
        if (arr.isPrivate()) {
            // Si el atributo es heredado y privado, es inaccesible
            if (arr.isInherited()) {
                throw new UnaccesibleVariableException(this.token);
            }

            // Si el atributo es llamado desde otro scope, es inaccesible
            if (!context.isSelfContext()) {
                throw new UnaccesibleVariableException(this.token);
            }
        }


        // Check que el array sea un array
        if (!arr.getType().isArray()) {
            throw new VariableIsNotArrayException(this.getToken());
        }

        // Check que el indice sea de tipo entero
        AttributeType indexType = index.getAttributeType(context.reset());
        if (!indexType.equals(AttributeType.IntType)) {
            throw new NonIntArrayIndexException(this.getToken());
        }

        return new AttributeType(arr.getType().getType());
    }

    public String generate(Context context, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.comment("access to array");

        helper.append(getArrayAddress(debug));

        helper.loadWord("$a0", "4($t0)"); // Access to element -- TODO

        return helper.getString();
    }

    public String accessVariable(Context context, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.comment("access to array");
        helper.append(getArrayAddress(debug));

        helper.loadAddress("$a0", "4($t0)"); // Access to element -- TODO

        return helper.getString();
    }

    private String getArrayAddress(boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.append(variable.loadWordByScope());
        helper.loadAddress("$t0", "($a0)"); // Access to cir

        // TODO check if valid index

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.arrayName, indentationIndex) + "," +
                JsonHelper.json("index", this.index, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
