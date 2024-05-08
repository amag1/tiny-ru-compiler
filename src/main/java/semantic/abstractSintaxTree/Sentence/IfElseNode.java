package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class IfElseNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode thenBody;
    private SentenceNode elseBody;

    public IfElseNode(ExpressionNode condition, SentenceNode thenBody, SentenceNode elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
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
