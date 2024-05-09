package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;

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
    public AttributeType getAttributeType() throws SemanticException {
        // TODO
        return new AttributeType(true, true, new Token("", Type.KW_IF, new Location()));
    }
}

