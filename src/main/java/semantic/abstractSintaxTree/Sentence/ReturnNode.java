package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;

public class ReturnNode extends SentenceNode{
    private ExpressionNode returnValue;


    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
