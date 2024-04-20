package semantic.symbolTable;

import lexical.Token;

public class AttributeType {
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
