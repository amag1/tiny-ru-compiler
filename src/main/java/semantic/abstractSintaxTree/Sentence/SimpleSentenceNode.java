package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class SimpleSentenceNode extends SentenceNode {
    private ExpressionNode expression;

    public SimpleSentenceNode(ExpressionNode expression) {
        this.nodeType = "simpleSentence";
        this.expression = expression;
    }

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", expression.toJson(indentationIndex), indentationIndex) + "," +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
