package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import location.Location;
import reader.Reader;
public class LexicalAnalyzer implements Lexical{
    private char[] chars;
    private final Location location;
    private boolean reachedEndOfFile = false;

    /**
     * @param reader El lector de caracteres
     */
    public LexicalAnalyzer(Reader reader) {
        this.chars = reader.getChars();
        // Inicializa una nueva location vacia
        this.location = new Location();
    }

    /**
     * @return El siguiente token
     * @throws LexicalException
     */
    @Override
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

    /**
     * @return El siguiente token de tipo ID_CLASS, dadod que ya se matcheó el primer caracter
     * @throws MalformedClassIdentifierException
     */
    private Token matchClassIdentifier() throws MalformedClassIdentifierException {
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

        if (!isLetter(currentChar)) {
            throw new MalformedClassIdentifierException(lexeme, location);
        }
        return new Token(lexeme, Type.ID_CLASS, location.copy());
    }

    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private boolean isUppercaseLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private boolean isLowercaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * @return Si se llegó al final del archivo
     */
    @Override
    public boolean isEndOfFile() {
        return this.location.getPosition() > (chars.length - 1);

    }


}
