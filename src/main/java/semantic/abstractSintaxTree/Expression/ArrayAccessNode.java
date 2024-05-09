package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

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
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.arrayName, indentationIndex) + "," +
                JsonHelper.json("index", this.index, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
