package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.LiteralNode;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnValue;

    public ReturnNode(ExpressionNode returnValue) {
        this.returnValue = returnValue;
    }

    public ReturnNode() {
        this.returnValue = null; // TODO: deberia devolver un tipo void
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
