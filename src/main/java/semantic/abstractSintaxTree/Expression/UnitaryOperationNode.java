package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class UnitaryOperationNode extends ExpressionNode{

    private ExpressionNode operating;
    private Operator operator;

    @Override
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return  new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
