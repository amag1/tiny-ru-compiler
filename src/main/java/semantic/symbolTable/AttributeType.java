package semantic.symbolTable;

import lexical.Token;
import semantic.Json;

/**
 * Representa un tipo de atributo de la tabla de simbolos
 */
public class AttributeType implements Json {
    public static AttributeType BoolType = new AttributeType("Bool");
    public static  AttributeType StrType = new AttributeType("Str");
    public static  AttributeType IntType = new AttributeType("Int");
    public static AttributeType CharType = new AttributeType("Char");
    public static AttributeType NilType = new AttributeType();

    private boolean isArray;
    private String type;
    private boolean isPrimitive;

    private boolean isNil;


    private Token token;

    public AttributeType(boolean isArray, boolean isPrimitive, Token token) {
        this.isArray = isArray;
        this.type = token.getLexem();
        this.isPrimitive = isPrimitive;
        this.token = token;
    }

    public AttributeType(String type) {
        this.isArray = false;
        this.type = type;
        this.isPrimitive = true;
    }

    public AttributeType() {
        this.isNil = true;
    }

    @Override
    public String toJson(int identationIndex) {
        String json =  "\"";

        if (this.isArray) {
            json += "Array ";
        }

        json += type;

        json +=  "\"";

        return json;
    }

    public Token getToken() {
        return token;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public String getType() {
        return type;
    }

    public boolean equals(AttributeType comparedAttributeType) {
        if (comparedAttributeType == null) return  false;

        return  this.isPrimitive == comparedAttributeType.isPrimitive &&
                this.isArray == comparedAttributeType.isArray &&
                this.type.equals(comparedAttributeType.type);
    }

    public boolean isNil() {
        return isNil;
    }

    public void setNil(boolean nil) {
        isNil = nil;
    }

}
