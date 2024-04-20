package exceptions.semantic;

import location.Location;
import semantic.symbolTable.ClassEntry;

public class RedefinedImplementException extends SemanticException {
    public RedefinedImplementException(ClassEntry currentClass, Location location) {
        super("Implement redefinido para el struct: " + currentClass.getName(), location);
    }
}
