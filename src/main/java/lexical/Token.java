package lexical;

import location.Location;

public class Token {
    private String lexeme;
    private Type type;
    private Location location;

    public Token(String lexeme, Type type, Location location) {
        this.lexeme = lexeme;
        this.type = type;
        this.location = location;
    }

    public String getLexem() {
        return lexeme;
    }

    public Type getType() {
        return type;
    }

    public int getLine() {
        return location.getLine();
    }

    public int getColumn() {
        return location.getColumn();
    }

}
