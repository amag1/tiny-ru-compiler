package exceptions.semantic;

import semantic.symbolTable.ClassEntry;

public class UndefinedImplException extends  SemanticException{
    public UndefinedImplException(ClassEntry currentClass) {
        super("No se definió una implementación para: " + currentClass.getName(), currentClass.getLocation());
    }
}
