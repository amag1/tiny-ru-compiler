package exceptions.semantic;

import semtantic.symbolTable.ClassEntry;
public class RedefinedConstructorException extends SemanticException {
    public RedefinedConstructorException(ClassEntry curentClass) {
        super("Ya se encontró un constructor definido para el struct: " + curentClass.getName(), curentClass.getLocation() );
    }
}
