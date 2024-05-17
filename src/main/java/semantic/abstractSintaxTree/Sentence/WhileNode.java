package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.AttributeType;

public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode loopBody;

    public WhileNode(ExpressionNode condition, SentenceNode loopBody) {
        this.nodeType = "whileSentence";
        this.condition = condition;
        this.loopBody = loopBody;

        setToken(condition.getToken());
    }

    @Override
    public void validate(Context context) throws AstException {
        AttributeType conditionType = condition.getAttributeType(context);
        // La condicion debe ser booleana
        if (!conditionType.getType().equals("Bool")) {
            throw new ParameterTypeMismatchException("Bool", conditionType.toString(), condition.getToken());
        }

        loopBody.validate(context);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("condition", condition, indentationIndex) + "," +
                JsonHelper.json("loopBody", loopBody, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
