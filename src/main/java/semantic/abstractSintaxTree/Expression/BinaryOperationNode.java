package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.BinaryTypeMismatchException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

import javax.management.Attribute;

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
        AttributeType leftType = leftOperating.getAttributeType(context.reset());
        AttributeType rightType = rightOperating.getAttributeType(context.reset());

        AttributeType returnType = this.operator.getAttributeType();
        AttributeType inputType = this.operator.getInputType();

        if (inputType.getType().equals("Any")) {
            // Si los tipos son primitivos, chequear que sean iguales
            // Si no lo son, la llamada es valida
            if (leftType.isPrimitive() && !leftType.getType().equals(rightType.getType())) {
                throw new BinaryTypeMismatchException(operator.getToken(), leftType.toString(), rightType.toString());
            }

            return returnType;
        }

        if (!leftType.getType().equals(inputType.getType())) {
            throw new BinaryTypeMismatchException(operator.getToken(), inputType.toString(), leftType.toString());
        }

        if (!rightType.getType().equals(inputType.getType())) {
            throw new BinaryTypeMismatchException(operator.getToken(), inputType.toString(), rightType.toString());
        }

        return returnType;
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
