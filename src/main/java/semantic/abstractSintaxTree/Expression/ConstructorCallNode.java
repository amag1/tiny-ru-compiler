package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.MethodCall;
import semantic.abstractSintaxTree.Expression.PrimaryNode;
import semantic.symbolTable.AttributeType;

import java.util.List;

public class ConstructorCallNode extends PrimaryNode implements MethodCall {
    private String className;
    private List<ExpressionNode> parameters;

    public ConstructorCallNode(String className) {
        this.className = className;
    }

    @Override
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        // TODO
        return "";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        this.parameters = parameters;
    }
}
