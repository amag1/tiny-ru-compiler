package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.UnaccesibleVariableException;
import exceptions.semantic.syntaxTree.UndeclaredVariableAccessException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.VariableEntry;

public class VariableAccessNode extends PrimaryNode {
    private String variableName;
    private Token token;

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

        // Si el atributo es heredado y privado, es inaccesible
        if (var.isInherited() && var.isPrivate()) {
            throw new UnaccesibleVariableException(token);
        }

        return var.getType();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.variableName, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
