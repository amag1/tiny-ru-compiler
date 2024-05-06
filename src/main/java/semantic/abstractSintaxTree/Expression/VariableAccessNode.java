package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class VariableAccessNode extends PrimaryNode {
    private String variableName;
    private boolean isSelf;

    public VariableAccessNode(String variableName, boolean isSelf) {
        this.variableName = variableName;
        this.isSelf = isSelf;
    }


    @Override
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        // TODO
        return "";
    }
}
