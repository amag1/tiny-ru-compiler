package lexical;

public class CharUtils {
    public static boolean isUppercaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean isLowercaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean isLetter(char c) {
        return isUppercaseLetter(c) || isLowercaseLetter(c);
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isWhitespace(char c) {
        // \u000b is vertical tab
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\u000b';
    }

    public static boolean isValidChar(char c) {
        // Common char types
        return isNumber(c)
                || isLetter(c)
                || isWhitespace(c)
                || isCommonSymbol(c)
                || isSpanishCharacter(c);
    }

    public static boolean isCommonSymbol(char c) {
        return c == '('
                || c == ')'
                || c == '{'
                || c == '}'
                || c == '['
                || c == ']'
                || c == '.'
                || c == ':'
                || c == ';'
                || c == ','
                || c == '\\'
                || c == '+'
                || c == '-'
                || c == '*'
                || c == '/'
                || c == '%'
                || c == '<'
                || c == '>'
                || c == '='
                || c == '!'
                || c == '&'
                || c == '|'
                || c == '^'
                || c == '~'
                || c == '?'
                || c == '@'
                || c == '#'
                || c == '$'
                || c == '_'
                || c == '¿';
    }

    public static boolean isSpanishCharacter(char c) {
        return c == 'á'
                || c == 'é'
                || c == 'í'
                || c == 'ó'
                || c == 'ú'
                || c == 'ñ';
    }
}
