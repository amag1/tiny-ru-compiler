package lexical;

public enum Type {
    // Identifiers
    ID_CLASS,
    ID,

    // Type
    TYPE_INT,
    TYPE_CHAR,
    TYPE_STRING,
    TYPE_BOOL,
    ARRAY,


    // Literals
    INT_LITERAL,
    CHAR_LITERAL,
    STRING_LITERAL,

    // Symbols
    OPEN_PAR,
    CLOSE_PAR,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    OPEN_CURLY,
    CLOSE_CURLY,
    DOT,
    COLON,
    SEMICOLON,
    COMMA,

    // Arithmetic Operators
    MULT,
    MOD,
    DIV,
    PLUS,
    MINUS,
    DPLUS,
    DMINUS,

    // Other operators
    ASSIGN,
    EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,
    NEG,
    NOT_EQUAL,

}
