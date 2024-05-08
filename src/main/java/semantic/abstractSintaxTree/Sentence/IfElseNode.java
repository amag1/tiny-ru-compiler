package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

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
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        String elseJson = "";
        if (elseBody != null) {
            elseJson = JsonHelper.json("elseBody", this.elseBody.toJson(indentationIndex), indentationIndex) + ",";
        }

        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("condition", this.condition.toJson(indentationIndex), indentationIndex) + "," +
                JsonHelper.json("thenBody", this.thenBody.toJson(indentationIndex), indentationIndex) + "," +
                elseJson +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
