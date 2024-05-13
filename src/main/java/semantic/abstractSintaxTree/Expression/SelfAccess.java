package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;

public class SelfAccess extends PrimaryNode {

    private PrimaryNode node;

    public SelfAccess(PrimaryNode node) {
        this.nodeType = "selfAccess";
        this.node = node;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", this.node, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }


    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Si la expresion es nula, el tipo es el de la clase
        if (this.node == null) {
            ClassEntry callingClass = context.getCallingClass();
            return new AttributeType(false, false, callingClass.getToken());
        }

        // Obtener atributo de la clase actual
        return node.getAttributeType(context.cloneSelfContext());

    }
}

