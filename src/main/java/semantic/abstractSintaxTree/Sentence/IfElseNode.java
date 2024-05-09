package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
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
    public void validate() throws AstException {
        // TODO
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
