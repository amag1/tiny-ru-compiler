package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.*;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.VariableEntry;

public class ArrayAccessNode extends PrimaryNode {
    private String arrayName;
    private ExpressionNode index;

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

        // Chequar si se puede acceder al atributo
        if (arr.isPrivate()) {
            // Si el atributo es heredado y privado, es inaccesible
            if (arr.isPrivate()) {
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

        // TODO: hacer esto un poco mas bonito
        return new AttributeType(arr.getType().getType());
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
