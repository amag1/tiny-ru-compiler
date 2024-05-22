package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.UnaryTypeMismatchException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

public class UnaryOperationNode extends ExpressionNode {

    private ExpressionNode operating;
    private Operator operator;

    public UnaryOperationNode(ExpressionNode operating, Token operator) {
        this.nodeType = "unaryOperation";
        this.operating = operating;
        this.operator = new Operator(operator);
        this.token = operator;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        String unaryOperator = operator.getToken().getLexem();
        Context operatingContext;
        if (unaryOperator.equals("++") || unaryOperator.equals("--")) {
           operatingContext = context.cloneOnlyVar();
        } else {
            operatingContext = context.reset();
        }

        AttributeType operatingType = operating.getAttributeType(operatingContext);

        AttributeType operatorType = this.operator.getAttributeType();
        if (operatorType.getType().equals(operatingType.getType())) {
            return operator.getAttributeType();
        }
        else {
            throw new UnaryTypeMismatchException(operator.getToken(), operatorType.toString(), operatingType.toString());
        }

    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("operating", this.operating, indentationIndex) + "," +
                JsonHelper.json("operator", this.operator, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
