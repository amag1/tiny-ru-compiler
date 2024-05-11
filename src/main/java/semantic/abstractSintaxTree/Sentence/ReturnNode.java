package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnValue;

    public ReturnNode(ExpressionNode returnValue) {
        this.nodeType = "returnSentence";
        this.returnValue = returnValue;
    }

    public ReturnNode() {
        this.nodeType = "returnSentence";
        this.returnValue = null; // TODO: deberia devolver un tipo void
    }

    @Override
    public void validate(Context context) throws AstException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("returnValue", returnValue, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
