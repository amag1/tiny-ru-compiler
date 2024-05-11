package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConstructorCallNode extends CallableNode {
    private Token className;

    public ConstructorCallNode(Token className) {
        super(className, className.getLexem());
        this.nodeType = "constructorCall";
        this.className = className;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Check that parameters match
        MethodEntry constructor = context.getConstructorByClass(this.className.getLexem());

        // Convertir parametros del construcor en una lista
        List<VariableEntry> parameters = new ArrayList<>();
        for (Map.Entry<String, VariableEntry> entry : constructor.getFormalParameters().entrySet()) {
            VariableEntry parameter = entry.getValue();
            parameters.add(parameter.getPosition(), parameter);
        }

        checkParametersMatch(context, parameters);

        return constructor.getReturnType();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("className", this.className.getLexem(), indentationIndex) + "," +
                JsonHelper.json("parameters", super.getParameters(), indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        super.setParameters(parameters);
    }
}
