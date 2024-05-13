package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.BinaryTypeMismatchException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
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
        this.token = leftOperating.getToken();
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        String leftType = leftOperating.getAttributeType(context).getType();
        String rightType = rightOperating.getAttributeType(context).getType();
        String operatorType = this.operator.getAttributeType().getType();

        if (!leftType.equals(operatorType)) {
            throw new BinaryTypeMismatchException(operator.getToken(), operatorType, leftType);
        }

        if (!rightType.equals(operatorType)) {
            throw new BinaryTypeMismatchException(operator.getToken(), operatorType, rightType);
        }

        return operator.getAttributeType();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("leftSide", this.leftOperating, indentationIndex) + "," +
                JsonHelper.json("operator", this.operator, indentationIndex) + "," +
                JsonHelper.json("rightSide", this.rightOperating, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
