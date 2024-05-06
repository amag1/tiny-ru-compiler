package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

public class ArrayAccessNode extends PrimaryNode {
    private String arrayName;
    private Token idToken;
    private ExpressionNode index;

    public ArrayAccessNode(Token arrayName) {
        this.arrayName = arrayName.getLexem();
        this.idToken = arrayName;
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
