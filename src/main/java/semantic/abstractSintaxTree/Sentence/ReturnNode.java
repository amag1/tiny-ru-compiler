package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.LiteralNode;

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
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        String returnJson = "void";
        if (returnValue != null) {
            returnJson = JsonHelper.json("returnValue", returnValue.toJson(indentationIndex), indentationIndex);
        }

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("returnValue", returnJson, indentationIndex) + "," +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
