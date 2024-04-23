package semantic.symbolTable;

import lexical.Token;
import semantic.Json;
import semantic.JsonHelper;

public class AttributeEntry extends VariableEntry implements Json {
    private boolean isPrivate;
    private boolean isInherited;

    public AttributeEntry(AttributeType type, Token token, boolean isPrivate) {
        super(type, token);
        this.isPrivate = isPrivate;
    }

    public String toJson() {
        return "{" +
            JsonHelper.json("name", this.getName()) + "," +
            JsonHelper.json("type",  this.getType().toJson()) + "," +
            JsonHelper.json("isPrivate", this.isPrivate)+ "," +
            JsonHelper.json("isInherited", this.isInherited) + "," +
            JsonHelper.json("position", this.getPosition()) +
            "}";
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isInherited() {
        return isInherited;
    }

    public void setInherited(boolean inherited) {
        isInherited = inherited;
    }

    public void setPosition(int position) {
        super.setPosition(position);
    }
}
