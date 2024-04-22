package semantic.symbolTable;

import lexical.Token;
import semantic.Json;

public class AttributeType implements Json {
    private boolean isArray;
    private String type;
    private boolean isPrimitive;
    private Token token;

    public AttributeType(boolean isArray, boolean isPrimitive, Token token) {
        this.isArray = isArray;
        this.type = token.getLexem();
        this.isPrimitive = isPrimitive;
        this.token = token;
    }

    @Override
    public String toJson() {
        return this.type; // TODO
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
}
