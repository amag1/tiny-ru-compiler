package exceptions.semantic.symbolTable;

import exceptions.TinyRuException;
import location.Location;

public class SymbolTableException extends TinyRuException {
    public SymbolTableException(String message, Location location) {
        super(message, location);
    }
}
