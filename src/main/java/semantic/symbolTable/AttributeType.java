package semantic.symbolTable;

import lexical.Token;
import semantic.Json;

/**
 * Representa un tipo de atributo de la tabla de simbolos
 */
public class AttributeType implements Json {
    public static AttributeType BoolType = new AttributeType("Bool");
    public static AttributeType StrType = new AttributeType("Str");
    public static AttributeType IntType = new AttributeType("Int");
    public static AttributeType CharType = new AttributeType("Char");
    public static AttributeType NilType = new AttributeType("nil");

    private boolean isArray;
    private String type;
    private boolean isPrimitive;
    private boolean isNil;
    private boolean isPredefined;


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
        return this.toString();
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
        if (comparedAttributeType == null) {
            return false;
        }

        return this.isPrimitive == comparedAttributeType.isPrimitive &&
                this.isArray == comparedAttributeType.isArray &&
                this.type.equals(comparedAttributeType.type);
    }

    public boolean isNil() {
        return isNil;
    }

    public void setNil(boolean nil) {
        isNil = nil;
    }

    public boolean isPredefined() {
        return isPredefined;
    }

    public void setPredefined(boolean predefined) {
        isPredefined = predefined;
    }

    public String toString() {
        String str = "";

        if (this.isArray) {
            str += "Array ";
        }

        str += type;

        return str;
    }

    public int getSize() {
        // Metodo para obtener el espacio necesario para almacenar el tipo de dato

        // TODO: no se como se vera esto para tipos no primitivos
        return 4;

    }

    public String getDefaultValue() {
        // Metodo para obtener el valor por defecto de un tipo de dato
        if (this.isPrimitive) {
            return switch (this.type) {
                case "Bool" -> "false";
                case "Char" -> "' '";
                case "Str" -> "\"\"";
                default -> "0";
            };
        }
        else {
            return "nil";
        }
    }
}
