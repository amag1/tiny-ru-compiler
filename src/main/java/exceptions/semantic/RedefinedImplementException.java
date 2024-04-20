package exceptions.semantic;

import lexical.Token;
import semtantic.symbolTable.ClassEntry;

public class RedefinedImplementException extends SemanticException {
    public RedefinedImplementException(ClassEntry currentClass) {
        super("Implement redefinido para el struct: " + currentClass.getName(), currentClass.getLocation());
    }
}
