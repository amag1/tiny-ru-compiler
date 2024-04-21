package semantic.symbolTable;

import semantic.Json;
import semantic.JsonHelper;

import java.util.*;

public class SymbolTable implements Json {
    private Map<String, ClassEntry> classes;
    private ClassEntry currentClass;
    private MethodEntry currentMethod;
    private MethodEntry start;

    public SymbolTable() {
        this.currentClass = null;
        this.currentMethod = null;
        this.start = new MethodEntry();
        this.classes = new TreeMap<String, ClassEntry>();
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{\n");
        json.append("\t\"classes\": ").append(JsonHelper.json(classes)).append(",\n");
        json.append("\t\"start\": ").append(start.toJson()).append("\n");
        json.append("}");

        return json.toString();
    }

    public ClassEntry getClassByName(String name) {
        return classes.get(name);
    }

    public void insertClass(ClassEntry classEntry) {
        classes.put(classEntry.getToken().getLexem(), classEntry);
    }

    public void setCurrentClass(ClassEntry currentClass) {
        this.currentClass = currentClass;
    }

    public ClassEntry getCurrentClass() {
        return currentClass;
    }
}
