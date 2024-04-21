package semantic.symbolTable;

import lexical.Token;
import semantic.Json;
import semantic.JsonHelper;

import java.util.Map;

public class MethodEntry implements Json {
    private String name;
    private Token token;
    private boolean isStatic;
    private boolean isInherited;
    private AttributeEntry returnType;
    private Map<String, VariableEntry> formalParameters;
    private Map<String, VariableEntry> localVariables;
    private int position;


    public String toJson() {
        StringBuilder json = new StringBuilder("{\n");

        if (this.name != null) {
            json.append("\t\"name\": \"").append(this.name).append("\",\n");
        }
        json.append("\t\"isStatic\": ").append(this.isStatic).append(",\n");
        json.append("\t\"isInherited\": ").append(this.isInherited).append(",\n");
        json.append("\t\"position\": ").append(this.position).append(",\n");
        if (this.returnType != null) {
            json.append("\t\"returnType\": ").append(this.returnType.toJson()).append(",\n");
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

}
