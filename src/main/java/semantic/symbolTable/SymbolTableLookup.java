package semantic.symbolTable;

public interface SymbolTableLookup {
    ClassEntry getCurrentClass();

    MethodEntry getCurrentMethod();

    VariableEntry getAttribute(String attributeName);

    ClassEntry getClassByName(String className);
}
