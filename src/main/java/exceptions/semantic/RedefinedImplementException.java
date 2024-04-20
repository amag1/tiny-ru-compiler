package exceptions.semantic;

import lexical.Token;
import location.Location;
import semtantic.symbolTable.ClassEntry;

public class RedefinedImplementException extends SemanticException {
    public RedefinedImplementException(ClassEntry currentClass, Location location) {
        super("Implement redefinido para el struct: " + currentClass.getName(), location);
    }
}
