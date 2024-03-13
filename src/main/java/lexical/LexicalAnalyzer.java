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
                if (isValidChar(currentChar)) {
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
            if (isValidChar(currentChar)) {
                lexeme += currentChar;
                location.increaseColumn();
                location.increasePosition();
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
        String lexeme = "";
        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        while (!reachedEndOfFile && (isLetter(currentChar) || isNumber(currentChar))) {
            lexeme += currentChar;
            consumePosition();
            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();

            }
        }

        // Last letter must be a letter
        char lastChar = lexeme.charAt(lexeme.length()-1);
        if (!isLetter(lastChar)){
            throw new MalformedClassIdentifierException(lexeme, location);
        }

        Token token = null;
        // Check if it is a type keyword
        if (lexeme.equals("Int") || lexeme.equals("Char") || lexeme.equals("String") || lexeme.equals("Bool")) {
            token = new Token(lexeme, Type.valueOf("TYPE_" + lexeme.toUpperCase()), startLocation);
        }

        if (lexeme.equals("Array")) {
            token = new Token(lexeme, Type.ARRAY, startLocation);
        }

        if (token == null){
            token = new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        return token;
    }

    private Token matchIntLiteral(Location startLocation) throws MalformedIntLiteralException{
        char currentChar = getCurrentChar();

        if (isEndOfFile()) {
            reachedEndOfFile = true;
            return new Token(currentChar, Type.INT_LITERAL, startLocation);
        }

        String lexeme = "";
        while (!reachedEndOfFile && isNumber(currentChar)) {
            lexeme += currentChar;
            consumePosition();

            if (isEndOfFile()) {
                reachedEndOfFile = true;
            } else {
                currentChar = getCurrentChar();
            }
        }

        if (isLetter(currentChar)) {
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


        while (!reachedEndOfFile && (isLetter(currentChar) || isNumber(currentChar))) {
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
        // Consumes the backslash
        char currentChar = getCurrentChar();
        String returnString = "";

        consumePosition();
        if (isEndOfFile()) {
            throw new InvalidCharacterException(currentChar, location);
        }
        currentChar = getCurrentChar();
        if (!isValidChar(currentChar)) {
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
