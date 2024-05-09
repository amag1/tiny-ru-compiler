package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.Json;
import semantic.JsonHelper;
import semantic.symbolTable.MethodEntry;

import java.util.HashMap;
import java.util.Map;

public class AstClassEntry implements Json {

    private String name;
    private Map<String, AstMethodEntry> methods;

    public AstClassEntry(String name) {
        this.name = name;
        this.methods = new HashMap<>();
    }

    public Map<String, AstMethodEntry> getMethods() {
        return methods;
    }

    public  void validateSentences() throws SemanticException {
        for (Map.Entry<String,AstMethodEntry> entry: methods.entrySet()) {
            AstMethodEntry currentMethod = entry.getValue();
            currentMethod.validateSentences();
        }
    }

    public String getName() {
        return name;
    }

    public  AstMethodEntry getMethod(String name) {return  methods.get(name);}

    public void addMethod(AstMethodEntry method) {methods.put(method.getName(), method);}

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("name", this.name, indentationIndex) + "," +
                JsonHelper.json("methods", this.methods, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
