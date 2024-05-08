package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class SimpleSentenceNode extends SentenceNode {
    private ExpressionNode expression;

    public SimpleSentenceNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return "";
    }
}
