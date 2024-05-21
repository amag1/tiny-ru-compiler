package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.VoidAccessException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

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

    public ChainNode(PrimaryNode parentNode, PrimaryNode childrenNode) {
        this.nodeType = "chain";
        this.parentNode = parentNode;
        this.childrenNode = childrenNode;
        this.token = parentNode.getToken();
    }

    public AttributeType getAttributeType(Context context) throws AstException {
        // Obtener el tipo del nodo padre
        AttributeType parentType = this.parentNode.getAttributeType(context);

        // Si el padre es de tipo void (null), entonces no existe el nodo hijo
        if (parentType == null || parentType.getType().equals("void")) {
            throw new VoidAccessException(this.token);
        }
        // Obtener el tipo del nodo hijo en base al contexto del padre
        Context childrendContext = context.cloneChainContext(parentType.getType());
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
}
