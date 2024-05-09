package exceptions.semantic.symbolTable;

import location.Location;
import semantic.symbolTable.MethodEntry;

public class RedefinedMethodException extends SymbolTableException {
    public RedefinedMethodException(MethodEntry method, Location location) {
        super("Metodo redefinido: " + method.getName(), location);
    }
}
