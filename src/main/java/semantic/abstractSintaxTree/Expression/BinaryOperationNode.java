package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.BinaryTypeMismatchException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;

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
    public AttributeType getAttributeType(SymbolTableLookup st) throws AstException {
        String leftType = leftOperating.getAttributeType(st).getType();
        String rightType = rightOperating.getAttributeType(st).getType();
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
