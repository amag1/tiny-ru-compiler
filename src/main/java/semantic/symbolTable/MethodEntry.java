package semantic.symbolTable;

import lexical.Token;
import location.Location;
import semantic.Json;
import semantic.JsonHelper;

import java.util.Map;
import java.util.TreeMap;

public class MethodEntry implements Json {
    private String name;
    private Token token;
    private boolean isStatic;
    private boolean isInherited;
    private AttributeType returnType;
    private Map<String, VariableEntry> formalParameters;
    private Map<String, VariableEntry> localVariables;
    private int position;
    private boolean isRedefined;


    public MethodEntry() {
        this.formalParameters = new TreeMap<String, VariableEntry>();
        this.localVariables = new TreeMap<String, VariableEntry>();
    }

    public MethodEntry(Token token, Boolean isStatic) {
        this.name = token.getLexem();
        this.token = token;
        this.formalParameters = new TreeMap<String, VariableEntry>();
        this.localVariables = new TreeMap<String, VariableEntry>();
        this.isStatic = isStatic;
        this.isInherited = false;
    }


    public String toJson(int identationIndex) {
        identationIndex++;

        String returnJson = "void";
        if (this.returnType != null) {
            returnJson = this.returnType.toJson(identationIndex);
        }

        String json = "{" +
                JsonHelper.json("name", this.name, identationIndex) + "," +
                JsonHelper.json("isStatic", this.isStatic, identationIndex) + "," +
                JsonHelper.json("isInherited", this.isInherited, identationIndex) + "," +
                JsonHelper.json("isRedefined", this.isRedefined, identationIndex) + "," +
                JsonHelper.json("position", this.position, identationIndex) + "," +
                JsonHelper.json("return", returnJson, identationIndex) + "," +
                JsonHelper.json("formalParameters", formalParameters, identationIndex) + "," +
                JsonHelper.json("localVariables", localVariables, identationIndex) +
                "\n" + JsonHelper.getIdentationString(identationIndex - 1) + "}";

        return json;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.token.getLocation();
    }

    public void setReturnType(AttributeType type) {
        this.returnType = type;
    }

    public VariableEntry getFormalParam(String name) {
        return this.formalParameters.get(name);
    }

    public void addFormalParam(VariableEntry param) {
        this.formalParameters.put(param.getName(), param);
    }

    public VariableEntry getLocalVariable(String name) {
        return this.localVariables.get(name);
    }

    public void addLocalVariable(VariableEntry variable) {
        this.localVariables.put(variable.getName(), variable);
    }

    public MethodEntry copy() {
        return new MethodEntry(this.token, this.isStatic);
    }

    public void setInherited(boolean isInherited) {
        this.isInherited = isInherited;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isRedefined() {
        return isRedefined;
    }

    public void setRedefined(boolean redefined) {
        isRedefined = redefined;
    }

    public boolean isInherited() {
        return isInherited;
    }

    public Map<String, VariableEntry> getFormalParameters() {
        return formalParameters;
    }

    public Map<String, VariableEntry> getLocalVariables() {
        return localVariables;
    }
}
