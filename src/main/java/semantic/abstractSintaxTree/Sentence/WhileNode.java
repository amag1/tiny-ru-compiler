package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.symbolTable.SymbolTableException;
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
    public void validate() throws SymbolTableException {
        // TODO
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
