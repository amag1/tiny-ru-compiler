package lexical;

public class Token {
    private String lexem;
    private Type type;
    private Location location;

    public Token(String lexem, Type type, Location location) {
        this.lexem = lexem;
        this.type = type;
        this.location = location;
    }

    public String getLexem() {
        return lexem;
    }

    public Type getType() {
        return type;
    }

}
