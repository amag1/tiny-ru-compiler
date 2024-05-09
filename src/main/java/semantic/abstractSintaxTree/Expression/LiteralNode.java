package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;


public class LiteralNode extends OperatingNode {
    private AttributeType attributeType;
    private String value;

    private Token token;

    public LiteralNode(Token token) {
        this.token = token;
        this.attributeType = switch (token.getType()) {
            case INT_LITERAL -> AttributeType.IntType;
            case STRING_LITERAL -> AttributeType.StrType;
            case CHAR_LITERAL -> AttributeType.CharType;
            case KW_TRUE, KW_FALSE -> AttributeType.BoolType;
            default -> AttributeType.NilType;
        };

        this.nodeType = "literal";
        this.value = token.getLexem();
    }

    @Override
    public AttributeType getAttributeType(SymbolTableLookup st) throws AstException {
        return this.attributeType;
    }
    
    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("attributeType", this.attributeType, indentationIndex) + "," +
                JsonHelper.json("value", this.value, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
