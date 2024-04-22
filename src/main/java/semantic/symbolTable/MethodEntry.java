package semantic.symbolTable;

import lexical.Token;
import location.Location;
import semantic.Json;
import semantic.JsonHelper;

import java.util.Map;

public class MethodEntry implements Json {
    private String name;
    private Token token;
    private boolean isStatic;
    private boolean isInherited;
    private AttributeType returnType;
    private Map<String, VariableEntry> formalParameters;
    private Map<String, VariableEntry> localVariables;
    private int position;

    public MethodEntry() {}

    public MethodEntry(Token token, Boolean isStatic) {
        this.name = token.getLexem();
        this.token = token;
        this.isStatic = isStatic;
        this.isInherited = false;
    }


    public String toJson() {
        StringBuilder json = new StringBuilder("{\n");

        if (this.name != null) {
            json.append("\t\"name\": \"").append(this.name).append("\",\n");
        }
        json.append("\t\"isStatic\": ").append(this.isStatic).append(",\n");
        json.append("\t\"isInherited\": ").append(this.isInherited).append(",\n");
        json.append("\t\"position\": ").append(this.position).append(",\n");
        if (this.returnType != null) {
            // json.append("\t\"returnType\": ").append(this.returnType.toJson()).append(",\n"); TODO
        }
        if (this.formalParameters != null) {
            json.append("\t\"formalParameters\": ").append(JsonHelper.json(formalParameters)).append(",\n");
        }
        if (this.localVariables != null) {
            json.append("\t\"localVariables\": ").append(JsonHelper.json(localVariables)).append(",\n");
        }

        // Remove the last comma and newline
        if (json.length() > 2) {
            json.delete(json.length() - 2, json.length());
        }

        json.append("\n}");
        return json.toString();
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {return  this.token.getLocation();}

    public void setReturnType(AttributeType type) {this.returnType = type;}

    public VariableEntry getFormalParam(String name) {return  this.formalParameters.get(name);}

    public void addFormalParam(VariableEntry param) {this.formalParameters.put(param.getName(), param)}
}
