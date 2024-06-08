package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.*;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.*;

import java.util.List;

/**
 * Nodo de llamada a método
 * <p>
 * Solo verifica que el metodo exista, lo demas lo maneja callableNode
 */
public class MethodCallNode extends CallableNode {
    private String methodName;

    private boolean isChained;

    private MethodEntry method;


    public MethodCallNode(Token methodName) {
        super(methodName);
        this.nodeType = "methodCall";
        this.methodName = methodName.getLexem();
        this.token = methodName;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Chequear que el contexto no sea oblyVar
        if (context.isOnlyVar()) {
            throw new OnlyVarException(this.token);
        }

        MethodEntry method = context.getMethod(this.methodName);
        this.isChained = context.isChain() || context.isSelfAccess();
        this.method = method;

        if (method == null) {
            throw new MethodNotFoundException(token);
        }

        if (context.isStatic() && !method.isStatic()) {
            throw new InvalidAccessInStaticContextException(token);
        }

        List<VariableEntry> parameters = method.getFormalParametersList();

        checkParametersMatch(context.reset(), parameters);

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

    public String generate(Context context, ClassEntry classEntry, MethodEntry method, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.comment("Start method call " + methodName);
        helper.push("$fp");

        // Pushear el objeto
        helper.comment("Pushear self");
        if (isChained) {
            // El objeto está en el acumulador
            helper.push("$a0");
        }
        else {
            // El objeto es self
            int offset = 4 + 4 * this.method.getFormalParametersList().size(); // Offset para self
            helper.loadWord("$a0", offset + "($fp)");
            helper.push("$a0");
        }

        // Pushea parametros
        helper.comment("Pushear parametros");
        for (ExpressionNode param : getParameters()) {
            String paramCode = param.generate(context, classEntry, method, debug);
            helper.append(paramCode);
            helper.push("$a0");
        }

        // Obtener ref a metodo
        helper.comment("Obtener ref a metodo");

        if (!context.isStatic()) {

            // Acceder al cir del objeto
            int offset = 4 + 4 * getParameters().size();
            helper.loadWord("$a0", offset + "($sp)");

            // Acceder a la VT de la clase
            helper.loadWord("$a0", "($a0)");

            // Llamar al método
            offset = this.method.getPosition() * 4;
            helper.loadWord("$t1", offset + "($a0)");
        }
        else {
            // Obtener directamente la VTable de la clase actual
            String vtable = helper.getVirtualTableName(classEntry);
            helper.loadAddress("$t1", vtable);
            int offset = this.method.getPosition() * 4;
            helper.loadWord("$t1", offset + "($t1)");
        }
        helper.jumpAndLinkRegister("$t1");

        // Resetear la stack
        // Popear todos los parametros
        helper.addIU("$sp", "$sp", 4 * getParameters().size());
        helper.pop("$t0"); // pop self
        helper.pop("$fp");

        return helper.getString();
    }
}
