package semantic.symbolTable;

import semantic.Json;
import semantic.JsonHelper;

import java.util.*;

public class SymbolTable implements Json {
    private final Map<String, ClassEntry> classes;
    private ClassEntry currentClass;
    private MethodEntry currentMethod;
    private final MethodEntry start;

    public SymbolTable() {
        this.currentClass = null;
        this.currentMethod = null;
        this.start = new MethodEntry(); // TODO
        this.classes = new TreeMap<String, ClassEntry>();
    }

    public String toJson() {
        return "{\n" + "\t\"classes\": " + JsonHelper.json(classes) + ",\n" +
                "\t\"start\": " + start.toJson() + "\n" +
                "}";
    }

    public List<ClassEntry> getClasses() {
        return new ArrayList<>(classes.values());
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

    public MethodEntry getCurrentMethod() {return currentMethod;}

    public void setCurrentMethod(MethodEntry method) {this.currentMethod = method;}
}

