package semantic.symbolTable;

import lexical.Token;
import location.Location;
import semantic.Json;
import semantic.JsonHelper;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Representa una clase dentro de la tabla de simbolos
 * Una clase tiene un conjunto de atributos y un conjunto de metodos, entre otros elementos
 */
public class ClassEntry implements Json {
    private String name;
    private Token token;
    private Map<String, VariableEntry> attributes;
    private Map<String, MethodEntry> methods;
    private boolean foundStruct;
    private boolean foundImpl;
    private String inherits;
    private boolean hasConstructor;
    private MethodEntry constructor;
    private boolean handledInheritance;
    private boolean isPredefined;


    public ClassEntry(Token token) {
        this.token = token;
        this.name = token.getLexem();
        this.inherits = "Object";
        this.foundStruct = false;
        this.foundImpl = false;
        this.hasConstructor = false;
        this.attributes = new TreeMap<String, VariableEntry>();
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

    public VariableEntry getAttribute(String name) {
        return attributes.get(name);
    }

    public TreeMap<String, VariableEntry> getAttributes() {
        return new TreeMap<>(attributes);
    }

    public void addAttribute(VariableEntry attribute) {
        int position = this.attributes.size();
        attribute.setPosition(position);
        attributes.put(attribute.getName(), attribute);
    }

    public void addInheritedAttributeAtPosition(VariableEntry attribute, int position) {
        // Increase position for all attributes that come after the new one
        for (VariableEntry attr : attributes.values()) {
            if (!attr.isInherited()) {
                attr.setPosition(attr.getPosition() + 1);
            }
        }

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

    public void addInheritedMethod(MethodEntry method) {
        // Increase position for all methods that come after the new one
        for (MethodEntry meth : methods.values()) {
            if (!meth.isInherited()) {
                meth.setPosition(meth.getPosition() + 1);
            }
        }
        methods.put(method.getName(), method);
    }

    public MethodEntry getMethod(String name) {
        return methods.get(name);
    }

    public TreeMap<String, MethodEntry> getMethods() {
        return new TreeMap<>(methods);
    }

    public boolean isPredefined() {
        return isPredefined;
    }

    public void setPredefined(boolean predefined) {
        isPredefined = predefined;
    }

    public List<MethodEntry> getMethodList() {
        List<MethodEntry> methodsList = new ArrayList<MethodEntry>();
        for (MethodEntry method : methods.values()) {
            methodsList.add(method);
        }

        methodsList.sort((MethodEntry m1, MethodEntry m2) -> m1.getPosition() - m2.getPosition());

        return methodsList;
    }

    public int getNumberOfBytes() {
        // 4 bytes para VT
        // 4 bytes para cada atributo
        return 4 + 4 * attributes.size();
    }

    public List<VariableEntry> getAttributesList() {
        List<VariableEntry> attributesList = new ArrayList<VariableEntry>();
        for (VariableEntry attribute : attributes.values()) {
            attributesList.add(attribute);
        }

        attributesList.sort((VariableEntry a1, VariableEntry a2) -> a1.getPosition() - a2.getPosition());

        return attributesList;
    }
}
