package semtantic.symbolTable;

import lexical.Token;

import java.util.Map;

public class ClassEntry {
    private String name;
    private Token token;
    private Map<Token, AttributeEntry> attributes;
    private Map<Token, MethodEntry> methods;
    private boolean foundStruct;
    private boolean foundImpl;
    private String inherits;
    private boolean hasConstructor;
    private MethodEntry constructor;


    public ClassEntry(Token token) {
        this.token = token;
        this.name = token.getLexem();
        this.inherits = "Object";
    }

    public Token getToken() {
        return token;
    }

    public void setFoundStruct(boolean foundStruct) {
        this.foundStruct = foundStruct;
    }

    public boolean isFoundStruct() {
        return foundStruct;
    }

    public boolean isFoundImpl() {
        return foundImpl;
    }

    public void setFoundImpl(boolean foundImpl) {
        this.foundImpl = foundImpl;
    }

    public String getInherits() {
        return inherits;
    }

    public void setInherits(String inherits) {
        this.inherits = inherits;
    }

    public boolean isHasConstructor() {
        return hasConstructor;
    }

    public void setHasConstructor(boolean hasConstructor) {
        this.hasConstructor = hasConstructor;
    }

    public MethodEntry getConstructor() {
        return constructor;
    }

    public void setConstructor(MethodEntry constructor) {
        this.constructor = constructor;
    }
}
