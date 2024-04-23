package semantic.symbolTable;

import lexical.Token;
import semantic.Json;
import semantic.JsonHelper;

public class VariableEntry implements Json {
    private String name;
    private AttributeType type;
    private Token token;
    private int position;

    public VariableEntry(AttributeType type, Token token, int position) {
        this.type = type;
        this.token = token;
        this.position = position;
        this.name = token.getLexem();
    }

    public VariableEntry(AttributeType type, Token token) {
        this.type = type;
        this.token = token;
        this.name = token.getLexem();
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJson() {
        return "{" +
                JsonHelper.json("name",  this.getName()) + "," +
                JsonHelper.json("type",  this.getType().getType()) + "," +
                JsonHelper.json("position", this.getPosition()) +
                "}";
    }
}
