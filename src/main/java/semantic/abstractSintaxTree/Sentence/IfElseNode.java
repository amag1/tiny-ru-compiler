package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class IfElseNode extends SentenceNode{
    private ExpressionNode condition;
    private SentenceNode thenBody;
    private SentenceNode elseBody;


    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
