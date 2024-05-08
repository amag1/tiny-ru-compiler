package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class BinaryOperationNode extends ExpressionNode {
    private ExpressionNode leftOperating;
    private ExpressionNode rightOperating;
    private Operator operator;

    public BinaryOperationNode(Token operator, ExpressionNode leftOperating, ExpressionNode rightOperating) {
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
        // TODO
        return "";
    }
}
