package exceptions.semantic.symbolTable;

import semantic.symbolTable.ClassEntry;

public class MissingConstructorException extends SymbolTableException {
    public MissingConstructorException(ClassEntry currentClass) {
        super("No se encontró el método constructor para el struct: " + currentClass.getName(), currentClass.getLocation());
    }
}
