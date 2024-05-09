package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

import java.util.List;

public class StaticMethodCallNode extends PrimaryNode implements MethodCall {
    private String methodName;
    private String className;
    private List<ExpressionNode> parameters;

    public StaticMethodCallNode(String className, String methodName) {
        this.nodeType = "staticMethodCall";
        this.methodName = methodName;
        this.className = className;
    }

    @Override
    public AttributeType getAttributeType() throws SymbolTableException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("className", this.className, indentationIndex) + "," +
                JsonHelper.json("methodName", this.methodName, indentationIndex) + "," +
                JsonHelper.json("parameters", this.parameters, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        this.parameters = parameters;
    }
}
