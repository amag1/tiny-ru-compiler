package exceptions.semantic;

import semantic.symbolTable.ClassEntry;

public class UndefinedStructException extends  SemanticException{
    public UndefinedStructException(ClassEntry currentClass) {
        super("No se definió un struct para: " + currentClass.getName(), currentClass.getLocation());
    }
}
