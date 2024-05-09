package exceptions.semantic.symbolTable;

import location.Location;
import semantic.symbolTable.ClassEntry;

public class RedefinedConstructorException extends SymbolTableException {
    public RedefinedConstructorException(ClassEntry curentClass, Location location) {
        super("Ya se encontró un constructor definido para el struct: " + curentClass.getName(), location);
    }
}
