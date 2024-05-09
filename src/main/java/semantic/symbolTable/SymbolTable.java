package semantic.symbolTable;

import semantic.Json;
import semantic.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Clase que representa la tabla de simbolos
 * Esta clase no implementa logica sobre la semantica del lenguaje
 * Se limita a brindar una estructura para almacenar y modificar la informacion de las clases y metodos
 */
public class SymbolTable implements Json, SymbolTableLookup {
    private final Map<String, ClassEntry> classes;
    private ClassEntry currentClass;
    private MethodEntry currentMethod;
    private final MethodEntry start;

    public SymbolTable() {
        this.currentClass = null;
        this.currentMethod = null;
        this.start = new MethodEntry();
        this.classes = new TreeMap<String, ClassEntry>();
    }

    public String toJson(int identationIndex) {
        identationIndex++;
        return "{" +
                JsonHelper.json("classes", classes, identationIndex) + "," +
                JsonHelper.json("start", start, identationIndex) +
                "\n" + JsonHelper.getIdentationString(identationIndex - 1) + "}";
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

    @Override
    public ClassEntry getCurrentClass() {
        return currentClass;
    }

    @Override
    public MethodEntry getCurrentMethod() {
        return currentMethod;
    }

    @Override
    public VariableEntry getAttribute(String attributeName) {
        // Buscar en variables locales -> paremetros -> atributos de la clase
        VariableEntry attribute = currentMethod.getLocalVariable(attributeName);
        if (attribute == null) {
            attribute = currentMethod.getFormalParam(attributeName);
            if (attribute == null) {
                attribute = currentClass.getAttribute(attributeName);
            }
        }

        return attribute;
    }

    public void setCurrentMethod(MethodEntry method) {
        this.currentMethod = method;
    }

    public MethodEntry getStart() {
        return this.start;
    }
}

