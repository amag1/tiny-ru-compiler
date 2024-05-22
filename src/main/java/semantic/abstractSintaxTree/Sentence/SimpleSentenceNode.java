package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.AttributeType;

/**
 * Nodo de sentencia simple
 */
public class SimpleSentenceNode extends SentenceNode {
    private ExpressionNode expression;

    public SimpleSentenceNode(ExpressionNode expression) {
        this.nodeType = "simpleSentence";
        this.expression = expression;

        if (expression != null) {
            setToken(expression.getToken());
        }
    }

    @Override
    public void validate(Context context) throws AstException {
        expression.getAttributeType(context);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
