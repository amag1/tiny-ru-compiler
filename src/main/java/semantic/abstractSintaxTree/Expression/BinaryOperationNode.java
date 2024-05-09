package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

public class BinaryOperationNode extends ExpressionNode {
    private ExpressionNode leftOperating;
    private ExpressionNode rightOperating;
    private Operator operator;

    public BinaryOperationNode(Token operator, ExpressionNode leftOperating, ExpressionNode rightOperating) {
        this.nodeType = "binaryOperation";
        this.leftOperating = leftOperating;
        this.rightOperating = rightOperating;
        this.operator = new Operator(operator);
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
                JsonHelper.json("leftSide", this.leftOperating, indentationIndex) + "," +
                JsonHelper.json("operator", this.operator, indentationIndex) + "," +
                JsonHelper.json("rightSide", this.rightOperating, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
