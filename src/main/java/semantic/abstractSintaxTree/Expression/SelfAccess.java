package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.BinaryTypeMismatchException;
import exceptions.semantic.syntaxTree.SelfAccessInStaticMethod;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de acceso a self
 */
public class SelfAccess extends PrimaryNode {

    private PrimaryNode node;

    public SelfAccess(PrimaryNode node, Token selfKeyword) {
        this.nodeType = "selfAccess";
        this.node = node;
        this.token = selfKeyword;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", this.node, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }


    /**
     * Valida que el contexto desde el que se llama sea valido
     * Si lo es, trata a la expresion que le sigue como un encadenado para obtener el tipo
     */
    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Lanzar error si se accede a self desde start
        if (context.isStart()) {
            throw new SelfAccessInStaticMethod(this.token);
        }

        // Lanzar error si se accede a self desde un metodo estático
        MethodEntry currentMethod = context.getCallingMethod();
        if (currentMethod.isStatic()) {
            throw new SelfAccessInStaticMethod(this.token);
        }

        // Si la expresion es nula, el tipo es el de la clase
        if (this.node == null) {
            ClassEntry callingClass = context.getCallingClass();
            return new AttributeType(false, false, callingClass.getToken());
        }

        // Obtener atributo de la clase actual
        Context selfContext = context.clone();
        selfContext.setSelfAccess(true);
        return node.getAttributeType(selfContext);
    }
}

