package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.symbolTable.AttributeType;

import java.util.List;

public class NewArrayNode extends PrimaryNode  {
    private AttributeType elementsType;
    private ExpressionNode lengthExpression;

    public NewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        this.lengthExpression = lengthExpression;
        this.elementsType = switch (elementsTypeToken.getType()) {
            case TYPE_INT -> AttributeType.IntType;
            case TYPE_STRING -> AttributeType.StrType;
            case TYPE_CHAR -> AttributeType.CharType;
            case TYPE_BOOL -> AttributeType.BoolType;
            default -> null; // Unreachable line: Only primitive types allowed
        };
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
