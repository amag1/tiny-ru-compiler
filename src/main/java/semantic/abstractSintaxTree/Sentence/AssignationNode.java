package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.PrimaryNode;

public class AssignationNode extends SentenceNode {
    private PrimaryNode leftSide;
    private ExpressionNode rightSide;

    public AssignationNode(PrimaryNode leftSide, ExpressionNode rightSide) {
        this.nodeType = "asignación";
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("leftSide", this.leftSide, indentationIndex) + "," +
                JsonHelper.json("rightSide", this.rightSide, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
