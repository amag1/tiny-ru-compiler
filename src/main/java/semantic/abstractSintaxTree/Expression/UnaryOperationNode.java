package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.UnaryTypeMismatchException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;

public class UnaryOperationNode extends ExpressionNode {

    private ExpressionNode operating;
    private Operator operator;

    public UnaryOperationNode(ExpressionNode operating, Token operator) {
        this.nodeType = "unaryOperation";
        this.operating = operating;
        this.operator = new Operator(operator);
    }

    @Override
    public AttributeType getAttributeType(SymbolTableLookup st) throws AstException {
        AttributeType operatingType = operating.getAttributeType(st);

        AttributeType operatorType = this.operator.getAttributeType();
        if (operatorType.getType().equals(operatingType.getType())) {
            return operator.getAttributeType();
        }
        else {
            throw new UnaryTypeMismatchException(operator.getToken(), operatorType.getType(), operatingType.getType());
        }

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
