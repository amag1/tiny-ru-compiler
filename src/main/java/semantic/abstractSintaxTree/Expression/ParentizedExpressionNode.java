package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

public class ParentizedExpressionNode extends PrimaryNode {

    private ExpressionNode expression;

    public ParentizedExpressionNode(ExpressionNode expression) {
        this.nodeType = "parentizedExpression";
        this.expression = expression;
        this.token = expression.getToken();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", this.expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        return expression.getAttributeType(context.reset());
    }
}
