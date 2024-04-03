package lexical;

import location.Location;

/**
 * Clase que representa un token.
 * Un token es un par de la forma (lexema, tipo).
 * El lexema es la cadena de caracteres que representa el token.
 */
public class Token {
    private String lexeme;
    private Type type;
    private Location location;

    public Token() {
    }

    public Token(String lexeme, Type type, Location location) {
        this.lexeme = lexeme;
        this.type = type;
        this.location = location;
    }

    public Token(char lexeme, Type type, Location location) {
        this.lexeme = String.valueOf(lexeme);
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

    public Location getLocation() {
        return location;
    }
}
