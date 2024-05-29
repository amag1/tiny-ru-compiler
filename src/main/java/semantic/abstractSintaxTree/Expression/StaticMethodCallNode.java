package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.*;
import exceptions.semantic.syntaxTree.ClassNotFoundException;
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

        checkParametersMatch(context.reset(), parameters);


        // Chequear que el contexto no sea oblyVar
        if (context.isOnlyVar()) {
            throw new OnlyVarException(this.token);
        }

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

    public  String generate(Context context, boolean debug) {
        // Buscar datos necesarios
        ClassEntry classEntry = context.getClass(this.className.getLexem());
        MethodEntry method = classEntry.getMethod(this.methodName.getLexem());

        MipsHelper helper = new MipsHelper(debug);

        // Actualiza el frame pointer
        helper.updateFramePointer();

        // Pushea parametros
        for (ExpressionNode param:getParameters()) {
            String paramCode = param.generate(context, debug);
            helper.append(paramCode);
            helper.push("$a0");
        }


        // Obtener el nombre de la virtual table
        String classVt = helper.getVirtualTableName(classEntry);

        // Calcular el offset
        int offset = method.getPosition()*4;

        // Jump a definición del método
        helper.loadAddress("$t0",classVt);
        helper.loadWord("$t1", offset+"($t0)");
        helper.jumpAndLinkRegister("$t1");

        return helper.getString();
    }
}
