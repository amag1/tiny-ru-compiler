package reader;

/**
 * Implementación de Reader que lee de una cadena de caracteres.
 */
public class StringReader implements Reader {
    private char[] chars;

    public StringReader(String input) {
        this.chars = input.toCharArray();
    }

    public char[] getChars() {
        return this.chars;
    }
}
