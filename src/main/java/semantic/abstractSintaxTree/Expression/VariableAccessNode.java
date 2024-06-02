package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.*;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.*;

public class VariableAccessNode extends PrimaryNode {
    private String variableName;
    private VariableEntry variable;

    public VariableAccessNode(Token token) {
        this.nodeType = "varAccess";
        this.token = token;
        this.variableName = token.getLexem();
    }


    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        VariableEntry var = context.getAttribute(this.variableName);
        if (var == null) {
            throw new UndeclaredVariableAccessException(token);
        }

        variable = var;

        // Chequar si se puede acceder al atributo
        if (var.isPrivate()) {
            // Si el atributo es heredado y privado, es inaccesible
            if (var.isInherited()) {
                throw new UnaccesibleVariableException(this.token);
            }

            // Si el atributo es llamado desde otro scope, es inaccesible
            if (!context.isSelfContext()) {
                throw new UnaccesibleVariableException(this.token);
            }
        }

        // Chequear si la variable es accesible desde el contexto actual
        if (context.isStatic()) {
            if (var.getScope() == Scope.CLASS) {
                throw new InvalidAccessInStaticContextException(this.token);
            }
        }

        return var.getType();
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        return variable.loadWordByScope(debug, methodEntry);
    }

    public String accessVariable(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        return variable.loadAddressByScope(debug, methodEntry);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.variableName, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
