package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ClassNotFoundException;
import exceptions.semantic.syntaxTree.OnlyVarException;
import lexical.Token;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Nodo de llamada a constructor
 */
public class ConstructorCallNode extends CallableNode {

    public ConstructorCallNode(Token className) {
        super(className, className.getLexem());
        this.nodeType = "constructorCall";
        this.token = className;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Verificar que la clase exista
        ClassEntry classEntry = context.getClass(this.token.getLexem());
        if (classEntry == null) {
            throw new ClassNotFoundException(this.token);
        }

        // Verificar que los parametros coincidan
        MethodEntry constructor = context.getConstructorByClass(this.token.getLexem());
        // Si el constructor es nulo, significa que la clase tiene un constructor vacío
        // Retorna un objeto del tipo de la clase
        if (constructor == null) {
            return new AttributeType(this.token.getLexem());
        }

        // Chequear que el contexto no sea onlyVar
        if (context.isOnlyVar()) {
            throw new OnlyVarException(this.token);
        }

        List<VariableEntry> parameters = constructor.getFormalParametersList();

        checkParametersMatch(context.reset(), parameters);

        return constructor.getReturnType();
    }

    public String generate(Context context, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);
        // Pushear frame pointer
        helper.push("$fp");
        // pushear los parametros
        for (int i = 0; i < super.getParameters().size(); i++) {
            helper.comment("Generar parametro " + i);
            helper.append(super.getParameters().get(i).generate(context, debug));
            helper.push("$a0");
        }

        // Llamar al constructor
        helper.comment("Llamar al constructor");
        helper.append("jal " + this.token.getLexem() + "_constructor");

        // Resetear stack
        helper.comment("Resetear stack");
        helper.addIU("$sp", "$sp", super.getParameters().size() * 4);

        // Resetear frame pointer
        helper.comment("Resetear frame pointer");
        helper.pop("$fp");

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("className", this.token.getLexem(), indentationIndex) + "," +
                JsonHelper.json("parameters", super.getParameters(), indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public void setParameters(List<ExpressionNode> parameters) {
        super.setParameters(parameters);
    }
}
