package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.PrimaryNode;
import semantic.symbolTable.AttributeType;

public class AssignationNode extends SentenceNode {
    private PrimaryNode leftSide;
    private ExpressionNode rightSide;

    public AssignationNode(PrimaryNode leftSide, ExpressionNode rightSide) {
        this.nodeType = "assignation";
        this.leftSide = leftSide;
        this.rightSide = rightSide;

        setToken(leftSide.getToken());
    }

    @Override
    public void validate(Context context) throws AstException {
        // Validar ambos lados y chequear que sean compatibles
        AttributeType leftType = leftSide.getAttributeType(context);
        AttributeType rightType = rightSide.getAttributeType(context);

        if (!context.checkTypes(leftType, rightType)) {
            throw new ParameterTypeMismatchException(leftType.toString(), rightType.toString(), rightSide.getToken());
        }
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
