package exceptions.semantic;

import semantic.symbolTable.ClassEntry;

public class MissingConstructorException extends SemanticException {
    public MissingConstructorException(ClassEntry currentClass) {
        super("No se encontró el método constructor para el struct: " + currentClass.getName(), currentClass.getLocation());
    }
}
