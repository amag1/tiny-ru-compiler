package lexical;

import exceptions.lexical.*;
import location.Location;
import reader.Reader;
public class LexicalAnalyzer implements Lexical{
    private final char[] chars;
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
        char currentChar = getCurrentChar();

        Token token;

        switch (currentChar) {
            // Symbols
            // Consume positiom
            case '(':
                consumePosition();
                token = new Token(currentChar, Type.OPEN_PAR, startLocation);
                break;
            case ')':
                consumePosition();
                token = new Token(currentChar, Type.CLOSE_PAR, startLocation);
                break;
            case '{':
                consumePosition();
                token = new Token(currentChar, Type.OPEN_CURLY, startLocation);
                break;
            case '}':
                consumePosition();
                token = new Token(currentChar, Type.CLOSE_CURLY, startLocation);
                break;
            case '[':
                consumePosition();
                token = new Token(currentChar, Type.OPEN_BRACKET, startLocation);
                break;
            case ']':
                consumePosition();
                token = new Token(currentChar, Type.CLOSE_BRACKET, startLocation);
                break;
            case '.':
                consumePosition();
                token = new Token(currentChar, Type.DOT, startLocation);
                break;
            case ':':
                consumePosition();
                token = new Token(currentChar, Type.COLON, startLocation);
                break;
            case ';':
                consumePosition();
                token = new Token(currentChar, Type.SEMICOLON, startLocation);
                break;
            case ',':
                consumePosition();
                token = new Token(currentChar, Type.COMMA, startLocation);
                break;
            case '\'':
                token = matchCharLiteral(startLocation);
                break;
            case '\"':
                token = matchStringLiteral(startLocation);
                break;

            // TODO operators

            // TODO char_literal

            // TODO string_literal

            default:
                token = matchComplexString(startLocation);
        }

        if (token.getType() == null) {
            throw new InvalidCharacterException(currentChar, startLocation);
        }

        return token;
    }

    private Token matchStringLiteral(Location startLocation) throws UnclosedStringLiteralException,MalformedStringLiteralException, InvalidCharacterException{
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            throw new UnclosedStringLiteralException("", location);
        }

        char currentChar = getCurrentChar();



        String lexeme = "";
        while (Character.compare(currentChar, '\"') != 0) {
            if (isEndOfFile()) {
                reachedEndOfFile = true;
                throw new UnclosedStringLiteralException(lexeme, location);
            }

            if (currentChar == '\\') {
                location.increaseColumn();
                location.increasePosition();
                currentChar = getCurrentChar();
                lexeme += matchEscapeChar();
            } else {
                if (isValidChar(currentChar)) {
                    lexeme += currentChar;
                    location.increaseColumn();
                    location.increasePosition();
                    currentChar = getCurrentChar();
                } else {
                    throw new MalformedStringLiteralException(lexeme, location);
                }
            }

        }
        consumePosition();
        return new Token(lexeme, Type.STRING_LITERAL, startLocation);
    }

    private Token matchCharLiteral(Location startLocation) throws MalformedCharLiteralException, UnclosedCharLiteralException, InvalidCharacterException {
        String lexeme = "";

        // Get next char
        char currentChar = getCurrentChar();

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            throw new UnclosedCharLiteralException(lexeme, location);
        }

        Token token;
        if (currentChar == '\\') {
            lexeme += matchEscapeChar();
        } else {
            if (isValidChar(currentChar)) {
                lexeme += currentChar;
                location.increaseColumn();
                location.increasePosition();
            } else {
                throw new MalformedCharLiteralException(lexeme, location);
            }
        }

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            throw new UnclosedCharLiteralException(lexeme, location);
        }

        currentChar = getCurrentChar();
        if (currentChar != '\'') {
            lexeme += currentChar;
            location.increaseColumn();
            location.increasePosition();
            throw new MalformedCharLiteralException(lexeme, location);
        }


        consumePosition();

        return new Token(lexeme, Type.CHAR_LITERAL, startLocation);
    }

    private Token matchComplexString(Location startLocation) throws LexicalException {
        Token token = new Token();
        char startChar = getCurrentChar();

        if (isNumber(startChar)) {
            token = matchIntLiteral(startLocation);
        }

        if (isLetter(startChar)) {
            if (isUppercaseLetter(startChar)) {
                // TODO check type declaration keywords (Char, Int, Bool, String)
                token = matchClassIdentifier(startLocation);
            } else {
                // TODO check keywords
                token = matchIdentifier(startLocation);
            }
        }
        return token;
    }

    /**
     * @return El siguiente token de tipo ID_CLASS, dadod que ya se matcheó el primer caracter
     * @throws MalformedClassIdentifierException
     */
    private Token matchClassIdentifier(Location startLocation) throws MalformedClassIdentifierException {
        char currentChar = getCurrentChar();
        String lexeme = "" + currentChar;
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        while (!reachedEndOfFile && (isLetter(currentChar) || isNumber(currentChar))) {
            consumePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();
                lexeme += currentChar;
            }
        }

        if (!isLetter(currentChar) && !isWhitespace(currentChar)){
            lexeme += currentChar;
            throw new MalformedClassIdentifierException(lexeme, location);
        }

        return new Token(lexeme, Type.ID_CLASS, startLocation);
    }

    private Token matchIntLiteral(Location startLocation) throws MalformedIntLiteralException{
        char currentChar = getCurrentChar();
        String lexeme = "" + currentChar;

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.INT_LITERAL, startLocation);
        }

        while (!reachedEndOfFile && isNumber(currentChar)) {
            consumePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();
                lexeme += currentChar;
            }
        }

        if (isLetter(currentChar)) {
            throw new MalformedIntLiteralException(lexeme, location);
        }

        return new Token(lexeme, Type.INT_LITERAL, startLocation);
    }

    private Token matchIdentifier(Location startLocation) throws LexicalException {
        char currentChar = getCurrentChar();
        String lexeme = "" + currentChar;

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID, startLocation);
        }


        while (!reachedEndOfFile && (isLetter(currentChar) || isNumber(currentChar))) {
            consumePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();
                lexeme += currentChar;
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
            char currentChar = getCurrentChar();
            while (isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    location.increaseLine();
                    location.increasePosition();
                    location.setColumn(0);
                } else {
                    consumePosition();
                }

                if (isEndOfFile()) {
                    reachedEndOfFile = true;
                } else {
                    currentChar = getCurrentChar();
                }
            }
        }
    }

    private String matchEscapeChar() throws InvalidCharacterException {
        char currentChar = getCurrentChar();
        String returnString = "";
        if (!isValidChar(currentChar)) {
            throw new InvalidCharacterException(currentChar, location);
        } else {
           switch (currentChar) {
               case '0':
                   throw new InvalidCharacterException('\0', location);
               case 'n':
                   returnString = "\n";
                   break;
               case 't':
                   returnString = "\t";
                   break;
               default:
                   returnString = "" + currentChar;
                   break;
           }
        }
        return returnString;
    }

    private boolean isValidChar(char c) {
        // Common char types
        return isNumber(c)
                || isLetter(c)
                || isWhitespace(c)
                || isCommonSymbol(c)
                || isSpanishCharacter(c);
    }

    private boolean isCommonSymbol(char c) {
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
                || c == '\''
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

    private boolean isSpanishCharacter(char c) {
        return c == 'á'
                || c == 'é'
                || c == 'í'
                || c == 'ó'
                || c == 'ú'
                || c == 'ñ';
    }

    private void consumePosition() {
        location.increaseColumn();
        location.increasePosition();
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

    private char getCurrentChar() {
        return this.chars[this.location.getPosition()];
    }
}
