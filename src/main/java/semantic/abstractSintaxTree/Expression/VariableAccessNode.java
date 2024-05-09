package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

public class VariableAccessNode extends PrimaryNode {
    private String variableName;

    public VariableAccessNode(String variableName) {
        this.nodeType = "varAccess";
        this.variableName = variableName;
    }


    @Override
    public AttributeType getAttributeType() throws SymbolTableException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.variableName, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
