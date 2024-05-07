package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class SelfAccess extends PrimaryNode {

    private PrimaryNode node;

    public SelfAccess(PrimaryNode node) {
        this.node = node;
    }

    public String toJson(int indentationIndex) {
        // TODO
        return "";
    }

    @Override
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }
}

