package semtantic.symbolTable;

import lexical.Token;

public class AttributeEntry extends VariableEntry {
    private boolean isPrivate;
    private boolean isInherited;

    public AttributeEntry(AttributeType type, Token token, boolean isPrivate) {
        super(type, token);
        this.isPrivate = isPrivate;
    }
}
