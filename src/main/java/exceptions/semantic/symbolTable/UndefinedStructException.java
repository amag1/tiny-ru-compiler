package exceptions.semantic.symbolTable;

import semantic.symbolTable.ClassEntry;

public class UndefinedStructException extends SymbolTableException {
    public UndefinedStructException(ClassEntry currentClass) {
        super("No se definió un struct para: " + currentClass.getName(), currentClass.getLocation());
    }
}
