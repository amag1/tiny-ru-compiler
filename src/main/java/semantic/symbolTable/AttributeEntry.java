package semantic.symbolTable;

import lexical.Token;
import semantic.Json;

public class AttributeEntry extends VariableEntry implements Json {
    private boolean isPrivate;
    private boolean isInherited;

    public AttributeEntry(AttributeType type, Token token, boolean isPrivate) {
        super(type, token);
        this.isPrivate = isPrivate;
    }

    public String toJson() {
        return "{\n" +
                "\t\"name\": \"" + this.getName() + "\",\n" +
                "\t\"type\": \"" + this.getType().getType() + "\",\n" +
                "\t\"isPrivate\": " + this.isPrivate + ",\n" +
                "\t\"isInherited\": " + this.isInherited + ",\n" +
                "\t\"position\": " + this.getPosition() + "\n" +
                "}";
    }
}
