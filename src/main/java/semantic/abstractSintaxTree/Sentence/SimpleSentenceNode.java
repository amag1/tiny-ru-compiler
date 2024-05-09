package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.symbolTable.SymbolTableException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class SimpleSentenceNode extends SentenceNode {
    private ExpressionNode expression;

    public SimpleSentenceNode(ExpressionNode expression) {
        this.nodeType = "simpleSentence";
        this.expression = expression;
    }

    @Override
    public void validate() throws SymbolTableException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
