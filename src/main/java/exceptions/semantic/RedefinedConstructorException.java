package exceptions.semantic;

import location.Location;
import semtantic.symbolTable.ClassEntry;
public class RedefinedConstructorException extends SemanticException {
    public RedefinedConstructorException(ClassEntry curentClass, Location location) {
        super("Ya se encontró un constructor definido para el struct: " + curentClass.getName(), location );
    }
}
