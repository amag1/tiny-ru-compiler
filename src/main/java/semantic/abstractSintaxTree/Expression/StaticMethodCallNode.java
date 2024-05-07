package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

import java.util.List;

public class StaticMethodCallNode extends PrimaryNode implements MethodCall {
    private String methodName;
    private String className;
    private List<ExpressionNode> parameters;

    public StaticMethodCallNode(String className, String methodName) {
        this.methodName = methodName;
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
