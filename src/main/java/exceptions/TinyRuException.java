package exceptions;

import location.Location;

public class TinyRuException extends Exception {
    private final Location location;

    public TinyRuException(String message, Location location) {
        super(message);
        this.location = location;
    }

    public int getLine() {
        return location.getLine();
    }

    public int getColumn() {
        return location.getColumn();
    }
}
