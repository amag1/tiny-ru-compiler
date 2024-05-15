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

    public SelfAccess() {
        this.nodeType = "selfAccess";
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Retornar el tipo es el de la clase a la que se hacer referencia
        ClassEntry callingClass = context.getCallingClass();
        return new AttributeType(false, false, callingClass.getToken());
    }
}

