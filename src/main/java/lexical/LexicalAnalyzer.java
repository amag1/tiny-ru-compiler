package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import location.Location;
import reader.Reader;
public class LexicalAnalyzer implements Lexical{
    private char[] chars;
    private Location location;
    private boolean reachedEndOfFile = false;

    public LexicalAnalyzer(Reader reader) {
        this.chars = reader.getChars();
        this.location = new Location(1, 0, 0);
    }

    public Token nextToken() throws LexicalException {
        char currentChar = chars[location.getPosition()];
        Token token = null;
        switch (currentChar) {
            default:
                if (isUppercaseLetter(currentChar)) {
                    token = matchClassIdentifier();
                }

                break;
        }

        return token;
    }

    public Token matchClassIdentifier() throws MalformedClassIdentifierException {
        String lexeme = "";
        char currentChar = chars[location.getPosition()]; ;
        while (!reachedEndOfFile && (isLetter(currentChar) || isDigit(currentChar))) {
            lexeme += currentChar;
            location.increaseColumn();
            location.increasePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = chars[location.getPosition()];
            }
        }

        if (!isLetter(chars[location.getPosition()-1])) {
            throw new MalformedClassIdentifierException(lexeme, location);
        }
        return new Token(lexeme, Type.ID_CLASS, location.copy());
    }

    public boolean isLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    public boolean isUppercaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public boolean isLowercaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    public boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    public boolean isEndOfFile() {
        return this.location.getPosition() > (chars.length - 1);

    }


}
