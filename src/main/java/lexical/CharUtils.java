package lexical;

/**
 * Clase totalmente estática con utilidades sobre caracteres
 * Presenta exclusivamente funciones que toman como argumento un caracter y devuelven un booleano
 */
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

    /**
     * @param c un caracter
     * @return un booleano para determinar si el caracter puede utilizarse como espacio en blanco dentro de un literal string o caracter
     */
    public static boolean isValidCharWhitespace(char c) {
        return c == ' ' || c == '\t';
    }

    /**
     * @param c caracter que deseamos saber si es valido
     * @return un boolean diciendo si es valido o no
     * Esta funcion solo deberia usarse para comments y strings
     */
    public static boolean isValidChar(char c) {
        // Common char types
        return isNumber(c)
                || isLetter(c)
                || isValidCharWhitespace(c)
                || isCommonSymbol(c)
                || isSpanishCharacter(c);
    }

    /**
     * @param c un caracter
     * @return si el caracter representa un simbolo válido de tinyRu o no.
     */
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
                || c == '¡'
                || c == '&'
                || c == '|'
                || c == '^'
                || c == '~'
                || c == '?'
                || c == '@'
                || c == '#'
                || c == '$'
                || c == '_'
                || c == '¿'
                || c == '\''
                || c == '\"';

    }

    /**
     * @param c un caracter
     * @return un booleano que indica si el caracter es una letra del alfabeto español.
     */
    public static boolean isSpanishCharacter(char c) {
        return c == 'á'
                || c == 'é'
                || c == 'í'
                || c == 'ó'
                || c == 'ú'
                || c == 'ñ'
                || c == 'Á'
                || c == 'É'
                || c == 'Í'
                || c == 'Ó'
                || c == 'Ú'
                || c == 'Ñ';
    }
}
