package semantic.symbolTable;

import lexical.Token;
import location.Location;
import semantic.Json;
import semantic.JsonHelper;

import java.util.Map;
import java.util.TreeMap;

public class ClassEntry implements Json {
    private String name;
    private Token token;
    private Map<String, AttributeEntry> attributes;
    private Map<String, MethodEntry> methods;
    private boolean foundStruct;
    private boolean foundImpl;
    private String inherits;
    private boolean hasConstructor;
    private MethodEntry constructor;
    private boolean handledInheritance;


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

    public String toJson(int identationIndex) {
        identationIndex++;
        String json = "{" +
                JsonHelper.json("name", this.name, identationIndex) + "," +
                JsonHelper.json("inherits", this.inherits, identationIndex) + "," +
                JsonHelper.json("foundStruct", this.foundStruct, identationIndex) + "," +
                JsonHelper.json("foundImpl", this.foundImpl, identationIndex) + "," +
                JsonHelper.json("hasConstructor", this.hasConstructor, identationIndex) + "," +
                JsonHelper.json("attributes", attributes, identationIndex) + "," +
                JsonHelper.json("methods", methods, identationIndex) + "," +
                JsonHelper.json("constructor", this.getConstructor(), identationIndex) +
                "\n" + JsonHelper.getIdentationString(identationIndex - 1) + "}";

        return json;
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

    public TreeMap<String, AttributeEntry> getAttributes() {
        return new TreeMap<>(attributes);
    }

    public void addAttribute(AttributeEntry attribute) {
        int position = this.attributes.size();
        attribute.setPosition(position);
        attributes.put(attribute.getName(), attribute);
    }

    public boolean handledInheritance() {
        return handledInheritance;
    }

    public void setHandledInheritance(boolean handledInheritance) {
        this.handledInheritance = handledInheritance;
    }

    public void addMethod(MethodEntry method) {
        int position = this.methods.size();
        method.setPosition(position);
        methods.put(method.getName(), method);
    }

    public MethodEntry getMethod(String name) {
        return methods.get(name);
    }

    public TreeMap<String, MethodEntry> getMethods() {
        return new TreeMap<>(methods);
    }
}
