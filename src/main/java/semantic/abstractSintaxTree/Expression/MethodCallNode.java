package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

import java.util.List;

public class MethodCallNode extends CallableNode {
    private String methodName;
    private List<ExpressionNode> parameters;

    public MethodCallNode(Token methodName) {
        super(methodName);
        this.nodeType = "methodCall";
        this.methodName = methodName.getLexem();
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
                JsonHelper.json("name", this.methodName, indentationIndex) + "," +
                JsonHelper.json("parameters", this.parameters, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        this.parameters = parameters;
    }
}
