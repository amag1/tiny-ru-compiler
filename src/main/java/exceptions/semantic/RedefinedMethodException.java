package exceptions.semantic;

import location.Location;
import semantic.symbolTable.MethodEntry;

public class RedefinedMethodException extends SemanticException {
    public RedefinedMethodException(MethodEntry method, Location location) {
        super("Metodo redefinido: " + method.getName(), location);
    }
}
