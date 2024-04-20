package semtantic.symbolTable;

import lexical.Token;
import location.Location;

import java.util.Map;
import java.util.TreeMap;

public class ClassEntry {
    private String name;
    private Token token;
    private Map<String, AttributeEntry> attributes;
    private Map<String, MethodEntry> methods;
    private boolean foundStruct;
    private boolean foundImpl;
    private String inherits;
    private boolean hasConstructor;
    private MethodEntry constructor;


    public ClassEntry(Token token) {
        this.token = token;
        this.name = token.getLexem();
        this.inherits = "Object";
        this.foundStruct = false;
        this.foundImpl = false;
        this.hasConstructor = false;
        this.attributes = new TreeMap<String, AttributeEntry>();
        this.methods = new TreeMap<String, MethodEntry>();
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

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.getToken().getLocation();
    }

    public AttributeEntry getAttribute(String name) {
        return attributes.get(name);
    }

    public void addAttribute(AttributeEntry attribute) {
        attributes.put(attribute.getName(), attribute);
    }
}
