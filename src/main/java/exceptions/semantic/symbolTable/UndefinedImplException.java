package exceptions.semantic.symbolTable;

import semantic.symbolTable.ClassEntry;

public class UndefinedImplException extends SymbolTableException {
    public UndefinedImplException(ClassEntry currentClass) {
        super("No se definió una implementación para: " + currentClass.getName(), currentClass.getLocation());
    }
}
