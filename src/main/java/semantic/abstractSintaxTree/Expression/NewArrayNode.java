package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.NonIntArrayIndexException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

public class NewArrayNode extends PrimaryNode {
    private AttributeType elementsType;
    private ExpressionNode lengthExpression;

    public NewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        this.nodeType = "newArray";
        this.token = elementsTypeToken;
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
    public AttributeType getAttributeType(Context context) throws AstException {
        // Validar que la expresion de la longitud se un entero
        AttributeType lengthExpressionType = this.lengthExpression.getAttributeType(context.reset());
        if (!context.checkTypes(AttributeType.IntType, lengthExpressionType)) {
            throw new NonIntArrayIndexException(this.token);
        }

        return this.elementsType;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("elementsType", this.elementsType, indentationIndex) + "," +
                JsonHelper.json("lengthExpression", this.lengthExpression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
