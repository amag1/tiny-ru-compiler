package lexical;

import exceptions.lexical.*;
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
        this.reachedEndOfFile = isEndOfFile();
    }

    /**
     * @return El siguiente token
     * @throws LexicalException
     */
    @Override
    public Token nextToken() throws LexicalException {
        removeWhitespaces();

        char currentChar = chars[location.getPosition()];
        Token token;
        switch (currentChar) {
            default:
                if (isNumber(currentChar)) {
                    token = matchIntLiteral();
                } else {
                    if (isLetter(currentChar)) {
                        if (isUppercaseLetter(currentChar)) {
                            token = matchClassIdentifier();
                        } else {
                            token = matchIdentifier();
                        }
                    } else {
                        throw new InvalidCharacterException(currentChar, location);
                    }
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
        while (!reachedEndOfFile && (isLetter(currentChar) || isNumber(currentChar))) {
            lexeme += currentChar;
            location.increaseColumn();
            location.increasePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = chars[location.getPosition()];
            }
        }

        if (!isLetter(currentChar) && !isWhitespace(currentChar)){
            throw new MalformedClassIdentifierException(lexeme, location);
        }
        return new Token(lexeme, Type.ID_CLASS, location.copy());
    }

    private Token matchIntLiteral() {
        String lexeme = "";
        char currentChar = chars[location.getPosition()];
        while (!reachedEndOfFile && isNumber(currentChar)) {
            lexeme += currentChar;
            location.increaseColumn();
            location.increasePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = chars[location.getPosition()];
            }
        }

        return new Token(lexeme, Type.INT_LITERAL, location.copy());
    }

    private Token matchIdentifier() throws LexicalException {
        String lexeme = "";
        char currentChar = chars[location.getPosition()];
        while (!reachedEndOfFile && (isLetter(currentChar) || isNumber(currentChar))) {
            lexeme += currentChar;
            location.increaseColumn();
            location.increasePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = chars[location.getPosition()];
            }
        }

        if (lexeme.length() > 1024) {
            throw new IdentifierTooLongException(lexeme, location);
        }

        return new Token(lexeme, Type.ID, location.copy());
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

    private boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private void removeWhitespaces() {
        if (!reachedEndOfFile) {
            char currentChar = chars[location.getPosition()];
            while (isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    location.increaseLine();
                    location.increasePosition();
                    location.setColumn(0);
                } else {
                    location.increaseColumn();
                    location.setPosition(location.getPosition() + 1);
                }

                if (isEndOfFile()) {
                    reachedEndOfFile = true;
                } else {
                    currentChar = chars[location.getPosition()];
                }
            }
        }
    }

    /**
     * @return Si se llegó al final del archivo
     */
    @Override
    public boolean isEndOfFile() {
        return this.location.getPosition() > (chars.length - 1);

    }

    @Override
    public int getColumn() {
        return this.location.getColumn();
    }

    @Override
    public int getLine() {
        return this.location.getLine();
    }


}
