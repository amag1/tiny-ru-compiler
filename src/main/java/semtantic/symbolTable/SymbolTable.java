package semtantic.symbolTable;

import lexical.Token;

import java.util.*;

public class SymbolTable {
    private Map<Token, ClassEntry> classes;
    private ClassEntry currentClass;
    private MethodEntry currentMethod;
    private MethodEntry start;

    public SymbolTable() {
        this.currentClass = null;
        this.currentMethod = null;
        this.start = null;
        this.classes = new TreeMap<>();

        // Init predefined classes
        List<ClassEntry> predefinedClasses = PredefinedClassCreator.generatePredefinedClasses();
        for (ClassEntry classEntry : predefinedClasses) {
            classes.put(classEntry.getToken(), classEntry);
        }
    }

    public void handleNewClass(Token token) {
        currentClass = new ClassEntry(token);
        currentClass.setFoundStruct(true);
        classes.put(token, currentClass);
        this.currentClass = currentClass;
    }
}
