package lexical;

public enum Type {
    // Identifiers
    ID_CLASS,
    ID,

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

    // Keywords
    KW_STRUCT,
    KW_IMPL,
    KW_ELSE,
    KW_FALSE,
    KW_IF,
    KW_RET, // Return
    KW_WHILE,
    KW_TRUE,
    KW_NIL,
    KW_NEW,
    KW_FN, // Function
    KW_ST, // Static
    KW_PRI, // Private
    KW_SELF,
    KW_VOID,

}
