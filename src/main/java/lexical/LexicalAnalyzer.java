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

        Token token;
        switch (currentChar) {
            // Symbols
            case '(':
                return new Token("", Type.OPEN_PAR, startLocation);
            case ')':
                return new Token("", Type.ID_CLASS, startLocation);
            case '{':
                return new Token("", Type.OPEN_CURLY, startLocation);
            case '}':
                return new Token("", Type.CLOSE_CURLY, startLocation);
            case '[':
                return new Token("", Type.OPEN_BRACKET, startLocation);
            case ']':
                return new Token("", Type.CLOSE_BRACKET, startLocation);
            case '.':
                return new Token("", Type.DOT, startLocation);
            case ':':
                return new Token("", Type.COLON, startLocation);
            case ';':
                return new Token("", Type.SEMICOLON, startLocation);

            // TODO operators
        }


        if (isNumber(currentChar)) {
            return matchIntLiteral();
        }

        if (isLetter(currentChar)) {
            if (isUppercaseLetter(currentChar)) {
                return matchClassIdentifier(currentChar, startLocation);
            } else {
                return matchIdentifier();
            }
        }

        throw new InvalidCharacterException(currentChar, startLocation);
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

        // La ultima letra de un struct debe estar en minuscula
        char lastChar = lexeme.charAt(lexeme.length()-1);
        if (!isLowercaseLetter(lastChar)) {
            throw new MalformedClassIdentifierException(lexeme, location);
        }

        return new Token(lexeme, Type.ID_CLASS, startLocation);
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


}
