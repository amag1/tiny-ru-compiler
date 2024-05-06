package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.PrimaryNode;

public class AssignationNode extends SentenceNode{
    private PrimaryNode leftSide;
    private ExpressionNode rightSide;


    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
