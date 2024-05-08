package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode loopBody;

    public WhileNode(ExpressionNode condition, SentenceNode loopBody) {
        this.nodeType = "whileSentence";
        this.condition = condition;
        this.loopBody = loopBody;
    }

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("condition", condition.toJson(indentationIndex), indentationIndex) + "," +
                JsonHelper.json("loopBody", loopBody.toJson(indentationIndex), indentationIndex) + "," +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
