package exceptions.semantic;

import lexical.Token;
import semtantic.symbolTable.ClassEntry;

public class MissingConstructorException extends SemanticException {
    public  MissingConstructorException(ClassEntry currentClass) {
        super("No se encontró el método constructor para el struct" + currentClass.getName(), currentClass.getLocation());
    }
}
