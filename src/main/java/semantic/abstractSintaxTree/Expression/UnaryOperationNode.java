package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

public class UnaryOperationNode extends ExpressionNode {

    private ExpressionNode operating;
    private Operator operator;

    public UnaryOperationNode(ExpressionNode operating, Token operator) {
        this.nodeType = "unaryOperation";
        this.operating = operating;
        this.operator = new Operator(operator);
    }

    @Override
    public AttributeType getAttributeType() throws AstException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("operating", this.operating, indentationIndex) + "," +
                JsonHelper.json("operator", this.operator, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
