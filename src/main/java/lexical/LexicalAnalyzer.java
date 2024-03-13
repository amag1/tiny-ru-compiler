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
     */
    @Override
    public Token nextToken() throws LexicalException {
        removeWhitespaces();
        // TODO: remove comments
        // TODO: remove tabs

        Location startLocation = location.copy();
        char currentChar = getCurrentChar();

        Token token = switch (currentChar) {
            // Symbols
            // Consume positiom
            case '(' -> {
                consumePosition();
                yield new Token(currentChar, Type.OPEN_PAR, startLocation);
            }
            case ')' -> {
                consumePosition();
                yield new Token(currentChar, Type.CLOSE_PAR, startLocation);
            }
            case '{' -> {
                consumePosition();
                yield new Token(currentChar, Type.OPEN_CURLY, startLocation);
            }
            case '}' -> {
                consumePosition();
                yield new Token(currentChar, Type.CLOSE_CURLY, startLocation);
            }
            case '[' -> {
                consumePosition();
                yield new Token(currentChar, Type.OPEN_BRACKET, startLocation);
            }
            case ']' -> {
                consumePosition();
                yield new Token(currentChar, Type.CLOSE_BRACKET, startLocation);
            }
            case '.' -> {
                consumePosition();
                yield new Token(currentChar, Type.DOT, startLocation);
            }
            case ':' -> {
                consumePosition();
                yield new Token(currentChar, Type.COLON, startLocation);
            }
            case ';' -> {
                consumePosition();
                yield new Token(currentChar, Type.SEMICOLON, startLocation);
            }
            case ',' -> {
                consumePosition();
                yield new Token(currentChar, Type.COMMA, startLocation);
            }
            case '*' -> {
                consumePosition();
                yield new Token(currentChar, Type.MULT, startLocation);
            }
            case '%' -> {
                consumePosition();
                yield new Token(currentChar, Type.MOD, startLocation);
            }
            case '/' -> {
                consumePosition();
                yield new Token(currentChar, Type.DIV, startLocation);
            }
            case '+' -> matchPlusSign(startLocation);
            case '-' -> matchMinusSign(startLocation);
            case '=' -> matchEqualSign(startLocation);
            case '>' -> matchGreaterThan(startLocation);
            case '<' -> matchLessSign(startLocation);
            case '!' -> matchNotEqual(startLocation);
            case '\'' -> matchCharLiteral(startLocation);
            case '\"' -> matchStringLiteral(startLocation);


            default -> matchComplexString(startLocation);
        };

        if (token.getType() == null) {
            throw new InvalidCharacterException(currentChar, startLocation);
        }

        return token;
    }

    private Token matchNotEqual(Location startLocation) {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token('!', Type.NEG, startLocation);
        }
        char currentChar = getCurrentChar();
        if (currentChar == '=') {
            consumePosition();
            return new Token("!=", Type.NOT_EQUAL, startLocation);
        }
        return new Token('!', Type.NEG, startLocation);
    }

    private Token matchGreaterThan(Location startLocation) {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token('>', Type.GREATER, startLocation);
        }
        char currentChar = getCurrentChar();
        if (currentChar == '=') {
            consumePosition();
            return new Token(">=", Type.GREATER_EQUAL, startLocation);
        }
        return new Token('>', Type.GREATER, startLocation);
    }

    private Token matchLessSign(Location startLocation) {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token('<', Type.LESS, startLocation);
        }
        char currentChar = getCurrentChar();
        if (currentChar == '=') {
            consumePosition();
            return new Token("<=", Type.LESS_EQUAL, startLocation);
        }
        return new Token('<', Type.LESS, startLocation);
    }



    private Token matchEqualSign(Location startLocation) {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token('=', Type.ASSIGN, startLocation);
        }
        char currentChar = getCurrentChar();
        if (currentChar == '=') {
            consumePosition();
            return new Token("==", Type.EQUAL, startLocation);
        }
        return new Token('=', Type.ASSIGN, startLocation);
    }

    private Token matchPlusSign(Location startLocation) {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token('+', Type.PLUS, startLocation);
        }
        char currentChar = getCurrentChar();
        if (currentChar == '+') {
            consumePosition();
            return new Token("++", Type.DPLUS, startLocation);
        }
        return new Token('+', Type.PLUS, startLocation);
    }

    private Token matchMinusSign(Location startLocation) {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token('-', Type.MINUS, startLocation);
        }
        char currentChar = getCurrentChar();
        if (currentChar == '-') {
            consumePosition();
            return new Token("--", Type.DMINUS, startLocation);
        }
        return new Token('-', Type.MINUS, startLocation);
    }

    private Token matchStringLiteral(Location startLocation) throws UnclosedStringLiteralException, MalformedStringLiteralException, InvalidCharacterException, StringLiteralTooLongException {
        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            throw new UnclosedStringLiteralException("\"", location);
        }
        char currentChar = getCurrentChar();
        String lexeme = "";
        while (currentChar != '\"') {
            if (currentChar == '\\') {
                lexeme += matchEscapeChar();
            } else {
                if (CharUtils.isValidChar(currentChar)) {
                    lexeme += currentChar;
                } else {
                    throw new MalformedStringLiteralException(lexeme, location);
                }
            }

            consumePosition();
            if (isEndOfFile()) {
                reachedEndOfFile = true;
                throw new UnclosedStringLiteralException(lexeme, location);
            }
            currentChar = getCurrentChar();

        }
        if (lexeme.length() > 1024) {
            throw new StringLiteralTooLongException(lexeme, location);
        }
        consumePosition();
        return new Token(lexeme, Type.STRING_LITERAL, startLocation);
    }

    private Token matchCharLiteral(Location startLocation) throws MalformedCharLiteralException, UnclosedCharLiteralException, InvalidCharacterException, EmptyCharLiteralException {
        // Start char should be '
        char currentChar = getCurrentChar();
        String lexeme = "";

        consumePosition();
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            throw new UnclosedCharLiteralException(lexeme, location);
        }

        Token token;

        currentChar = getCurrentChar();
        if (currentChar == '\\') {
            lexeme += matchEscapeChar();
        } else {
            if (CharUtils.isValidChar(currentChar)) {
                lexeme += currentChar;
                consumePosition();
            } else {
                if (currentChar == '\'') {
                    throw new EmptyCharLiteralException(location);
                }

                if (currentChar == '\0') {
                    throw new InvalidCharacterException(currentChar, location);
                }
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
            throw new MalformedCharLiteralException(lexeme, location);
        }

        consumePosition();

        return new Token(lexeme, Type.CHAR_LITERAL, startLocation);
    }

    private Token matchComplexString(Location startLocation) throws LexicalException {
        Token token = new Token();
        char startChar = getCurrentChar();

        if (CharUtils.isNumber(startChar)) {
            token = matchIntLiteral(startLocation);
        }

        if (CharUtils.isLetter(startChar)) {
            if (CharUtils.isUppercaseLetter(startChar)) {
                token = matchClassIdentifier(startLocation);
            } else {
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
        String lexeme = "";
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        while (!reachedEndOfFile && (CharUtils.isLetter(currentChar) || CharUtils.isNumber(currentChar))) {
            lexeme += currentChar;
            consumePosition();
            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();

            }
        }

        // Last letter must be a letter
        char lastChar = lexeme.charAt(lexeme.length() - 1);
        if (!CharUtils.isLetter(lastChar)) {
            throw new MalformedClassIdentifierException(lexeme, location);
        }

        Token token = null;
        // Check if it is a type keyword
        if (PrimitiveType.getType(lexeme) != null) {
            token = new Token(lexeme, PrimitiveType.getType(lexeme), startLocation);
        } else {
            token = new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        return token;
    }

    private Token matchIntLiteral(Location startLocation) throws MalformedIntLiteralException {
        char currentChar = getCurrentChar();

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(currentChar, Type.INT_LITERAL, startLocation);
        }

        String lexeme = "";
        while (!reachedEndOfFile && CharUtils.isNumber(currentChar)) {
            lexeme += currentChar;
            consumePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();
            }
        }

        if (CharUtils.isLetter(currentChar)) {
            throw new MalformedIntLiteralException(lexeme + currentChar, location);
        }

        return new Token(lexeme, Type.INT_LITERAL, startLocation);
    }

    private Token matchIdentifier(Location startLocation) throws LexicalException {
        char currentChar = getCurrentChar();
        String lexeme = "";

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID, startLocation);
        }


        while (!reachedEndOfFile && (CharUtils.isLetter(currentChar) || CharUtils.isNumber(currentChar))) {
            lexeme += currentChar;
            consumePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();
            }
        }

        if (lexeme.length() > 1024) {
            throw new IdentifierTooLongException(lexeme, location);
        }

        Token token = null;

        // Check if it is a keyword
        if (Keyword.getKeywordType(lexeme) != null) {
            token = new Token(lexeme, Keyword.getKeywordType(lexeme), startLocation);
        } else {
            token = new Token(lexeme, Type.ID, startLocation);
        }

        return token;
    }


    private void removeWhitespaces() {
        if (!reachedEndOfFile) {
            char currentChar = getCurrentChar();
            while (CharUtils.isWhitespace(currentChar)) {
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
        // Consumes the backslash
        char currentChar = getCurrentChar();
        String returnString = "";

        consumePosition();
        if (isEndOfFile()) {
            throw new InvalidCharacterException(currentChar, location);
        }
        currentChar = getCurrentChar();
        if (!CharUtils.isValidChar(currentChar)) {
            throw new InvalidCharacterException(currentChar, location);
        } else {
            returnString = switch (currentChar) {
                case '0' -> throw new InvalidCharacterException(currentChar, location);
                case 'n' -> "\n";
                case 't' -> "\t";
                default -> "" + currentChar;
            };
        }
        return returnString;
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
