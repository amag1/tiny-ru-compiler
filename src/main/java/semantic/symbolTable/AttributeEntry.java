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

    public String toJson(int identationIndex) {
        identationIndex ++;

        return "{" +
            JsonHelper.json("name", this.getName(), identationIndex) + "," +
            JsonHelper.json("type",  this.getType().toJson(identationIndex), identationIndex) + "," +
            JsonHelper.json("isPrivate", this.isPrivate, identationIndex)+ "," +
            JsonHelper.json("isInherited", this.isInherited, identationIndex) + "," +
            JsonHelper.json("position", this.getPosition(), identationIndex) +
            "\n" + JsonHelper.getIdentationString(identationIndex-1) + "}";
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
