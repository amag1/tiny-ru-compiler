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
        // TODO: remove comments
        // TODO: remove tabs

        Location startLocation = location.copy();
        char currentChar = chars[location.getPosition()];

        // Consume posicion
        location.increaseColumn();
        location.increasePosition();

        Token token = new Token();

        switch (currentChar) {
            // Symbols
            case '(':
                token = new Token("", Type.OPEN_PAR, startLocation);
                break;
            case ')':
                token = new Token("", Type.ID_CLASS, startLocation);
                break;
            case '{':
                token = new Token("", Type.OPEN_CURLY, startLocation);
                break;
            case '}':
                token = new Token("", Type.CLOSE_CURLY, startLocation);
                break;
            case '[':
                token = new Token("", Type.OPEN_BRACKET, startLocation);
                break;
            case ']':
                token = new Token("", Type.CLOSE_BRACKET, startLocation);
                break;
            case '.':
                token = new Token("", Type.DOT, startLocation);
                break;
            case ':':
                token = new Token("", Type.COLON, startLocation);
                break;
            case ';':
                token = new Token("", Type.SEMICOLON, startLocation);
                break;
            case ',':
                token = new Token("", Type.COMMA, startLocation);
                break;

            // TODO operators

            // TODO char_literal

            // TODO string_literal

            default:
                token = matchComplexString(currentChar, startLocation);
        }

        if (token.getType() == null) {
            throw new InvalidCharacterException(currentChar, startLocation);
        }

        return token;
    }

    private Token matchComplexString(char startChar, Location startLocation) throws LexicalException {
        Token token = new Token();


        if (isNumber(startChar)) {
            token = matchIntLiteral(startChar, startLocation);
        }

        if (isLetter(startChar)) {
            if (isUppercaseLetter(startChar)) {
                // TODO check type declaration keywords (Char, Int, Bool, String)
                token = matchClassIdentifier(startChar, startLocation);
            } else {
                // TODO check keywords
                token = matchIdentifier(startChar, startLocation);
            }
        }


        return token;
    }

    /**
     * @return El siguiente token de tipo ID_CLASS, dadod que ya se matcheó el primer caracter
     * @throws MalformedClassIdentifierException
     */
    private Token matchClassIdentifier(char startChar, Location startLocation) throws MalformedClassIdentifierException {
        String lexeme = "" + startChar;

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID_CLASS, startLocation);
        }

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

        if (!isLetter(currentChar) && !isWhitespace(currentChar)){
            lexeme += currentChar;
            throw new MalformedClassIdentifierException(lexeme, location);
        }

        return new Token(lexeme, Type.ID_CLASS, startLocation);
    }

    private Token matchIntLiteral(char startChar, Location startLocation) throws MalformedIntLiteralException{
        String lexeme = "" + startChar;

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.INT_LITERAL, startLocation);
        }

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

        if (isLetter(currentChar)) {
            throw new MalformedIntLiteralException(lexeme, location);
        }

        return new Token(lexeme, Type.INT_LITERAL, startLocation);
    }

    private Token matchIdentifier(char startChar, Location startLocation) throws LexicalException {
        String lexeme = "" + startChar;

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID, startLocation);
        }

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

        return new Token(lexeme, Type.ID, startLocation);
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
