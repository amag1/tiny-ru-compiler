package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class ChainNode extends PrimaryNode {
    private PrimaryNode parentNode;
    private PrimaryNode childrenNode;

    public ChainNode(PrimaryNode parentNode, PrimaryNode childrenNode) {
        this.parentNode = parentNode;
        this.childrenNode = childrenNode;
    }

    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return  new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
