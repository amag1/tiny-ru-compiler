package reader;

public class StringReader implements Reader{
    private String input;
    private char[] chars;

    public StringReader(String input) {
        this.input = input;
        this.chars = input.toCharArray();
    }

    public char[] getChars() {
        return this.chars;
    }
}
