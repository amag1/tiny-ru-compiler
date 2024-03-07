package exceptions.lexical;

import location.Location;

public class LexicalException extends Exception{
    private Location location;
    public LexicalException(String message, Location location) {

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
