package exceptions.semantic.symbolTable;

import location.Location;
import semantic.symbolTable.ClassEntry;

public class RedefinedImplementException extends SymbolTableException {
    public RedefinedImplementException(ClassEntry currentClass, Location location) {
        super("Implement redefinido para el struct: " + currentClass.getName(), location);
    }
}
