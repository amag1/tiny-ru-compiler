package semtantic.symbolTable;

import lexical.Token;

import java.util.*;

public class SymbolTable {
    private Map<String, ClassEntry> classes;
    private ClassEntry currentClass;
    private MethodEntry currentMethod;
    private MethodEntry start;

    public SymbolTable() {
        this.currentClass = null;
        this.currentMethod = null;
        this.start = null;
        this.classes = new TreeMap<>();
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
