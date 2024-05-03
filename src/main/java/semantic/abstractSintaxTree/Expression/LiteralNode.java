package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import lexical.Token;
import lexical.Type;
import semantic.symbolTable.AttributeType;


public class LiteralNode extends OperatingNode{
    public LiteralNode(Token token) {
        this.attributeType = switch (token.getType()) {
            case INT_LITERAL -> AttributeType.IntType;
            case STRING_LITERAL -> AttributeType.StrType;
            case CHAR_LITERAL -> AttributeType.CharType;
            case KW_TRUE, KW_FALSE -> AttributeType.BoolType;
            default -> AttributeType.NilType;
        };
    }
    @Override
    public AttributeType getAttributeType() throws SemanticException {
       return this.attributeType;
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
