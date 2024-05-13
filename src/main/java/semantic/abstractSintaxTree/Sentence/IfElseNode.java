package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.AttributeType;

public class IfElseNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode thenBody;
    private SentenceNode elseBody;

    public IfElseNode(ExpressionNode condition, SentenceNode thenBody, SentenceNode elseBody) {
        this.nodeType = "ifSentence";
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public void validate(Context context) throws AstException {
        AttributeType conditionType = condition.getAttributeType(context);
        // La condicion debe ser booleana
        if (!conditionType.getType().equals("Bool")) {
            throw new ParameterTypeMismatchException("Bool", conditionType.getType(), condition.getToken());
        }

        int emptyBranches = 2;
        if (thenBody != null) {
            thenBody.validate(context);
            emptyBranches--;
        }

        if (elseBody != null) {
            elseBody.validate(context);
            emptyBranches--;
        }


        if (emptyBranches > 0 && thenBody.hasReturn() && elseBody.hasReturn()) {
            setReturn(true);
        }
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("condition", this.condition, indentationIndex) + "," +
                JsonHelper.json("thenBody", this.thenBody, indentationIndex) + "," +
                JsonHelper.json("elseBody", this.elseBody, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
