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
}
