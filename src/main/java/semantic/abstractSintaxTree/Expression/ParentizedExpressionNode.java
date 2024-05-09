package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

public class ParentizedExpressionNode extends PrimaryNode {

    private ExpressionNode expression;

    public ParentizedExpressionNode(ExpressionNode expression) {
        this.nodeType = "parentizedExpression";
        this.expression = expression;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", this.expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    @Override
    public AttributeType getAttributeType() throws SymbolTableException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }
}
