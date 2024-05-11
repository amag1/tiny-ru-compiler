package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

import java.util.List;

public class StaticMethodCallNode extends CallableNode {
    private Token methodName;
    private Token className;
    private List<ExpressionNode> parameters;

    public StaticMethodCallNode(Token className, Token methodName) {
        super(methodName);
        this.nodeType = "staticMethodCall";
        this.methodName = methodName;
        this.className = className;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("className", this.className.getLexem(), indentationIndex) + "," +
                JsonHelper.json("methodName", this.methodName.getLexem(), indentationIndex) + "," +
                JsonHelper.json("parameters", this.parameters, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        this.parameters = parameters;
    }
}
