package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ClassNotFoundException;
import exceptions.semantic.syntaxTree.InvalidStaticMethodCallException;
import exceptions.semantic.syntaxTree.MethodNotFoundException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;
import semantic.symbolTable.VariableEntry;

import java.util.List;

public class StaticMethodCallNode extends CallableNode {
    private Token methodName;
    private Token className;

    public StaticMethodCallNode(Token className, Token methodName) {
        super(methodName);
        this.nodeType = "staticMethodCall";
        this.methodName = methodName;
        this.className = className;
        this.token = className;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Obtener la clase por nombre
        ClassEntry classEntry = context.getClass(this.className.getLexem());
        if (classEntry == null) {
            throw new ClassNotFoundException(className);
        }

        // Obtener el metodo por nombre
        MethodEntry method = classEntry.getMethod(this.methodName.getLexem());
        if (method == null) {
            throw new MethodNotFoundException(methodName);
        }

        // Chequear que el metodo es estatico
        if (!method.isStatic()) {
            throw new InvalidStaticMethodCallException(methodName);
        }

        // Chequear que los parametros coincidan
        List<VariableEntry> parameters = method.getFormalParametersList();

        checkParametersMatch(context, parameters);

        return method.getReturnType();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("className", this.className.getLexem(), indentationIndex) + "," +
                JsonHelper.json("methodName", this.methodName.getLexem(), indentationIndex) + "," +
                JsonHelper.json("parameters", super.getParameters(), indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        super.setParameters(parameters);
    }
}
