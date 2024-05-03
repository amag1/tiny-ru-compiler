package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class VariableAccessNode extends PrimaryNode{
    private String variableName;

    public VariableAccessNode(String variableName) {
        this.variableName = variableName;
    }


    @Override
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return  new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
