package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.*;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.VariableEntry;

public class ArrayAccessNode extends PrimaryNode {
    private String arrayName;
    private Token idToken;
    private ExpressionNode index;

    public ArrayAccessNode(Token arrayName, ExpressionNode index) {
        this.nodeType = "arrayAccess";
        this.arrayName = arrayName.getLexem();
        this.idToken = arrayName;
        this.index = index;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Check que el array exista
        VariableEntry arr = context.getAttribute(this.arrayName);
        if (arr == null) {
            throw new UndeclaredVariableAccessException(this.idToken);
        }

        // Setear el acceso self en falso
        context.setSelf(false);

        // Chequar si se puede acceder al atributo
        if (arr.isPrivate()) {
            // Si el atributo es heredado y privado, es inaccesible
            if (arr.isPrivate()) {
                throw new UnaccesibleVariableException(this.idToken);
            }

            // Si el atributo es llamado desde otro scope, es inaccesible
            if (!context.isCallingClassScope()) {
                throw new UnaccesibleVariableException(this.idToken);
            }
        }


        // Check que el array sea un array
        if (!arr.getType().isArray()) {
            throw new VariableIsNotArrayException(this.idToken);
        }

        // Check que el indice sea de tipo entero
        AttributeType indexType = index.getAttributeType(context);
        if (!indexType.equals(AttributeType.IntType)) {
            throw new NonIntArrayIndexException(this.idToken);
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
