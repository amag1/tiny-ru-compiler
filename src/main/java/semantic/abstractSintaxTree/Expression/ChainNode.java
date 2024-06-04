package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.VoidAccessException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo encadenado
 * <p>
 * Representa un nodo que se encadena con otro nodo
 * <p>
 * Ambos nodos son de tipo primario
 */
public class ChainNode extends PrimaryNode {
    private PrimaryNode parentNode;
    private PrimaryNode childrenNode;

    AttributeType parentType;

    public ChainNode(PrimaryNode parentNode, PrimaryNode childrenNode) {
        this.nodeType = "chain";
        this.parentNode = parentNode;
        this.childrenNode = childrenNode;
        this.token = parentNode.getToken();
    }

    public AttributeType getAttributeType(Context context) throws AstException {
        // Obtener el tipo del nodo padre
        Context parentContext = context.clone();
        parentContext.setOnlyVar(false);
        this.parentType = this.parentNode.getAttributeType(parentContext);

        // Si el padre es de tipo void (null), entonces no existe el nodo hijo
        if (parentType == null || parentType.getType().equals("void")) {
            throw new VoidAccessException(this.token);
        }
        // Obtener el tipo del nodo hijo en base al contexto del padre
        Context childrendContext = context.clone();
        childrendContext.setCurrentClassName(parentType.getType());
        return childrenNode.getAttributeType(childrendContext);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("parentNode", this.parentNode, indentationIndex) + "," +
                JsonHelper.json("childrenNode", this.childrenNode, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        // Generate code for the parent node and store it in the accumulator
        helper.comment("Generate code for the parent node");
        helper.append(this.parentNode.generate(context, classEntry, methodEntry, debug));

        // TODO validar no nil

        // Generar código para el hijo
        Context childrendContext = context.clone();
        childrendContext.setCurrentClassName(parentType.getType());
        helper.append(this.childrenNode.generate(context, classEntry, methodEntry, debug));

        return helper.getString();
    }
}
