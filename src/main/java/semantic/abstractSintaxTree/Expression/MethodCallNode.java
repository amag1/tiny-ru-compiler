package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.MethodNotFoundException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.*;

import java.util.List;

public class MethodCallNode extends CallableNode {
    private String methodName;

    private Token token;

    public MethodCallNode(Token methodName) {
        super(methodName);
        this.nodeType = "methodCall";
        this.methodName = methodName.getLexem();
        this.token = methodName;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        MethodEntry method = context.getMethod(this.methodName);

        // Setear el acceso self en falso
        context.setSelf(false);

        if (method == null) {
            throw new MethodNotFoundException(token);
        }

        List<VariableEntry> parameters = method.getFormalParametersList();

        checkParametersMatch(context, parameters);

        return method.getReturnType();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.methodName, indentationIndex) + "," +
                JsonHelper.json("parameters", super.getParameters(), indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        super.setParameters(parameters);
    }
}
