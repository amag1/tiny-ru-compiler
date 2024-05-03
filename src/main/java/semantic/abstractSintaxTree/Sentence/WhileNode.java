package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class WhileNode extends SentenceNode{
    private ExpressionNode condition;
    private SentenceNode loopBody;


    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
