package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class StaticMethodCallNode extends PrimaryNode{
    private String methodName;
    private ExpressionNode[] parameters;
    @Override
    public AttributeType getType() throws SemanticException {
        // TODO
        return  new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
