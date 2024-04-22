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

    public String toJson() {
        StringBuilder json = new StringBuilder("{\n");
        json.append("\t\"name\": \"").append(this.name).append("\",\n");
        json.append("\t\"inherits\": \"").append(this.inherits).append("\",\n");
        json.append("\t\"foundStruct\": ").append(this.foundStruct).append(",\n");
        json.append("\t\"foundImpl\": ").append(this.foundImpl).append(",\n");
        json.append("\t\"hasConstructor\": ").append(this.hasConstructor).append(",\n");
        json.append("\t\"attributes\": ").append(JsonHelper.json(attributes)).append(",\n");
        json.append("\t\"methods\": ").append(JsonHelper.json(methods)).append("\n");
        json.append("}");

        return json.toString();
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
        attributes.put(attribute.getName(), attribute);
    }

    public boolean handledInheritance() {
        return handledInheritance;
    }

    public void setHandledInheritance(boolean handledInheritance) {
        this.handledInheritance = handledInheritance;
    }

    public void addMethod(MethodEntry method) {methods.put(method.getName(), method);}

    public MethodEntry getMethod(String name) {return methods.get(name);}

    public void replaceMethod(MethodEntry method) {this.methods.replace(method.getName(), method);}
}
