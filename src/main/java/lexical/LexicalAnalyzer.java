package lexical;

import exceptions.lexical.*;
import location.Location;
import reader.Reader;

/**
 * Clase que implementa la interfaz Lexical.
 * Se encarga de matchear los tokens del archivo.
 * Además, se encarga de manejar los comentarios y los espacios en blanco.
 * Para esto, implementa una estrategia `switch-case` con diferentes métodos que matchean los tokens.
 */
public class LexicalAnalyzer implements Lexical {
    private final char[] chars;
    private final Location location;

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
     * @throws LexicalException una excepción léxica detallando el error.
     *                          <p>
     *                          El método se encarga de matchear el siguiente token del archivo.
     *                          Si se llegó al final del archivo, retorna null.
     *                          Si se encuentra un caracter inválido, lanza una excepción.
     *                          También lanza excepciones si al encontrar errores al matchear tokens.
     */
    @Override
    public Token nextToken() throws LexicalException {
        // Si se llegó al final del archivo, retorna null
        if (isEndOfFile()) {
            return null;
        }

        char currentChar = getCurrentChar();
        // Se encarga de remover los espacios en blanco y los comentarios
        while (!isEndOfFile() && (CharUtils.isWhitespace(currentChar) || isCommentStart())) {
            if (CharUtils.isWhitespace(currentChar)) {
                removeWhitespaces();
            }

            if (isCommentStart()) {
                removeComments();
            }

            if (!isEndOfFile()) {
                currentChar = getCurrentChar();
            }
        }

        // Si se llegó al final del archivo, retorna null
        if (isEndOfFile()) {
            return null;
        }

        // Objeto location que guarda la posición inicial del token
        Location startLocation = location.copy();
        currentChar = getCurrentChar();

        // Matchea el token
        Token token = switch (currentChar) {
            // Símbolos
            case '(' -> createToken(Type.OPEN_PAR);
            case ')' -> createToken(Type.CLOSE_PAR);
            case '{' -> createToken(Type.OPEN_CURLY);
            case '}' -> createToken(Type.CLOSE_CURLY);
            case '[' -> createToken(Type.OPEN_BRACKET);
            case ']' -> createToken(Type.CLOSE_BRACKET);
            case '.' -> createToken(Type.DOT);
            case ':' -> createToken(Type.COLON);
            case ';' -> createToken(Type.SEMICOLON);
            case ',' -> createToken(Type.COMMA);

            // Operadores
            case '*' -> createToken(Type.MULT);
            case '%' -> createToken(Type.MOD);
            case '/' -> createToken(Type.DIV);
            case '+' -> matchPlusSign();
            case '-' -> matchMinusSign();
            case '=' -> matchEqualSign();
            case '>' -> matchGreaterThan();
            case '<' -> matchLessSign();
            case '!' -> matchNotEqual();
            case '|', '&' -> matchTwoSymbolsOrFail(currentChar);

            // Caracteres
            case '\'' -> matchCharLiteral();

            // Strings
            case '\"' -> matchStringLiteral();

            // Identificdore y palabras reservadas
            default -> matchComplexString();
        };

        if (token.getType() == null) {
            throw new InvalidCharacterException(currentChar, location);
        }

        return token;
    }

    private Token createToken(Type type) {
        Location startLocation = location.copy();
        char currentChar = getCurrentChar();
        consumePosition();
        return new Token(currentChar, type, startLocation);
    }

    private Token matchNotEqual() {
        return matchOneOrTwoCharToken('!', '=');
    }

    private Token matchGreaterThan() {
        return matchOneOrTwoCharToken('>', '=');
    }

    private Token matchLessSign() {
        return matchOneOrTwoCharToken('<', '=');
    }

    private Token matchEqualSign() {
        return matchOneOrTwoCharToken('=', '=');
    }

    private Token matchPlusSign() {
        return matchOneOrTwoCharToken('+', '+');
    }

    private Token matchMinusSign() {
        Location startLocation = location.copy();
        try {
            if (peekNextChar() == '>') {
                consumePosition();
                consumePosition();
                return new Token("->", Type.ARROW, startLocation);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return matchOneOrTwoCharToken('-', '-');
    }

    private Token matchOneOrTwoCharToken(char firstChar, char secondChar) {
        Location startLocation = location.copy();
        StringBuilder sb = new StringBuilder();
        sb.append(firstChar);
        consumePosition();
        if (isEndOfFile()) {
            String lexeme = sb.toString();
            return new Token(lexeme, PredefinedLexemeMap.getType(lexeme), startLocation);
        }

        char currentChar = getCurrentChar();
        if (currentChar == secondChar) {
            consumePosition();
            String lexeme = sb.append(secondChar).toString();
            return new Token(lexeme, PredefinedLexemeMap.getType(lexeme), startLocation);
        }
        String lexeme = sb.toString();
        return new Token(lexeme, PredefinedLexemeMap.getType(lexeme), startLocation);
    }

    private Token matchTwoSymbolsOrFail(char charToMatch) throws MalformedDoubleSymbolException {
        Location startLocation = location.copy();
        char currentChar = getCurrentChar();
        if (currentChar != charToMatch) {
            throw new MalformedDoubleSymbolException(charToMatch, location);
        }
        consumePosition();
        if (!isEndOfFile()) {
            currentChar = getCurrentChar();
            if (currentChar == charToMatch) {
                consumePosition();
                String lexeme = "" + charToMatch + charToMatch;
                return new Token(lexeme, PredefinedLexemeMap.getType(lexeme), startLocation);
            }
        }

        throw new MalformedDoubleSymbolException(charToMatch, location);
    }

    private Token matchStringLiteral() throws UnclosedStringLiteralException, MalformedStringLiteralException, InvalidCharacterException, StringLiteralTooLongException {
        Location startLocation = location.copy();
        consumePosition();
        if (isEndOfFile()) {
            throw new UnclosedStringLiteralException("\"", location);
        }
        char currentChar = getCurrentChar();
        String lexeme = "";
        while (currentChar != '\"') {
            if (currentChar == '\\') {
                lexeme += matchEscapeChar();
            }
            else {
                if (CharUtils.isValidChar(currentChar)) {
                    lexeme += currentChar;
                }
                else {
                    throw new MalformedStringLiteralException(lexeme, location);
                }
            }

            consumePosition();
            if (isEndOfFile()) {
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

    private Token matchCharLiteral() throws MalformedCharLiteralException, UnclosedCharLiteralException, InvalidCharacterException, EmptyCharLiteralException {
        Location startLocation = location.copy();
        // Start char should be '
        char currentChar = getCurrentChar();
        String lexeme = "";

        consumePosition();
        if (isEndOfFile()) {
            throw new UnclosedCharLiteralException(lexeme, location);
        }

        Token token;

        currentChar = getCurrentChar();
        if (currentChar == '\\') {
            lexeme += matchEscapeChar();
            consumePosition();
        }
        else {
            if (currentChar == '\'') {
                throw new EmptyCharLiteralException(location);
            }

            if (currentChar == '\0') {
                throw new InvalidCharacterException(currentChar, location);
            }

            if (CharUtils.isValidChar(currentChar)) {
                lexeme += currentChar;
                consumePosition();
            }
            else {
                throw new MalformedCharLiteralException(lexeme, location);
            }
        }

        if (isEndOfFile()) {
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

    private Token matchComplexString() throws LexicalException {
        Location startLocation = location.copy();

        Token token = new Token();
        char startChar = getCurrentChar();

        if (CharUtils.isNumber(startChar)) {
            // Match literal int
            token = matchIntLiteral(startLocation);
        }

        if (CharUtils.isLetter(startChar)) {
            if (CharUtils.isUppercaseLetter(startChar)) {
                // Match identificador de clase
                token = matchClassIdentifier(startLocation);
            }
            else {
                // Match identificador de método o atributo
                token = matchIdentifier(startLocation);
            }
        }
        return token;
    }

    /**
     * @return El siguiente token de tipo ID_CLASS, dadod que ya se matcheó el primer caracter
     * @throws MalformedClassIdentifierException Si el identificador de clase es inválido
     */
    private Token matchClassIdentifier(Location startLocation) throws MalformedClassIdentifierException {
        char currentChar = getCurrentChar();
        String lexeme = "";
        if (isEndOfFile()) {
            return new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        // Da por hecho que el primer caracter es una letra mayúscula
        // Añade caraceres mientras sean letras, números o guiones bajos
        while (!isEndOfFile() && (CharUtils.isLetter(currentChar) || CharUtils.isNumber(currentChar) || currentChar == '_')) {
            lexeme += currentChar;
            consumePosition();
            if (!isEndOfFile()) {
                currentChar = getCurrentChar();
            }
        }

        // El último carater debe ser una letra
        char lastChar = lexeme.charAt(lexeme.length() - 1);
        if (!CharUtils.isLetter(lastChar)) {
            throw new MalformedClassIdentifierException(lexeme, location);
        }

        Token token = null;
        // Verificar si la palabra es un tipo primitivo
        if (PrimitiveType.getType(lexeme) != null) {
            token = new Token(lexeme, PrimitiveType.getType(lexeme), startLocation);
        }
        else {
            // De otro modo es un identificador de clase
            token = new Token(lexeme, Type.ID_CLASS, startLocation);
        }

        return token;
    }

    /**
     * @param startLocation La posición inicial del token
     * @return El siguiente token de tipo INT_LITERAL
     * @throws MalformedIntLiteralException Si el literal entero es inválido
     */
    private Token matchIntLiteral(Location startLocation) throws MalformedIntLiteralException {
        char currentChar = getCurrentChar();

        if (isEndOfFile()) {
            return new Token(currentChar, Type.INT_LITERAL, startLocation);
        }

        String lexeme = "";
        // Añade caracteres al lexema mientras sean números
        while (!isEndOfFile() && CharUtils.isNumber(currentChar)) {
            lexeme += currentChar;
            consumePosition();

            if (!isEndOfFile()) {
                currentChar = getCurrentChar();
            }
        }

        // Lanza un error si hay letras a continuación
        // Por ejemplo, 123a
        if (CharUtils.isLetter(currentChar)) {
            throw new MalformedIntLiteralException(lexeme + currentChar, location);
        }

        return new Token(lexeme, Type.INT_LITERAL, startLocation);
    }


    /**
     * @param startLocation La posición inicial del token
     * @return El siguiente token de tipo ID
     * @throws IdentifierTooLongException si el identificador supera el límte de 1024 caracteres
     */
    private Token matchIdentifier(Location startLocation) throws IdentifierTooLongException {
        char currentChar = getCurrentChar();
        String lexeme = "";

        if (isEndOfFile()) {
            return new Token(lexeme, Type.ID, startLocation);
        }


        while (!isEndOfFile() && (CharUtils.isLetter(currentChar) || CharUtils.isNumber(currentChar) || currentChar == '_')) {
            lexeme += currentChar;
            consumePosition();

            if (!isEndOfFile()) {
                currentChar = getCurrentChar();
            }
        }


        if (lexeme.length() > 1024) {
            throw new IdentifierTooLongException(lexeme, location);
        }

        Token token = null;

        // Verificar si se trata de una palabra clave
        if (Keyword.getKeywordType(lexeme) != null) {
            token = new Token(lexeme, Keyword.getKeywordType(lexeme), startLocation);
        }
        else {
            token = new Token(lexeme, Type.ID, startLocation);
        }

        return token;
    }


    /**
     * Remueve los espacios en blanco
     * Avanza la posición de la location hasta el próximo caracter no-whitespace
     */
    private void removeWhitespaces() {
        char currentChar = getCurrentChar();
        while (!isEndOfFile() && CharUtils.isWhitespace(currentChar)) {
            if (currentChar == '\n') {
                location.increaseLine();
                location.increasePosition();
                location.setColumn(1);
            }
            else {
                consumePosition();
            }

            if (!isEndOfFile()) {
                currentChar = getCurrentChar();
            }
        }
    }

    /**
     * @return El string asociado al caracter de escape
     * @throws InvalidCharacterException Si el caracter de escape es inválido
     */
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
        }
        else {
            returnString = switch (currentChar) {
                // \0 es el caracter nulo
                case '0' -> throw new InvalidCharacterException(currentChar, location);
                // Los caracteres \n, \t y \v son especiales
                case 'n' -> "\n";
                case 't' -> "\t";
                case 'v' -> "" + (char) 11;
                // Los demás caracteres simplemente se devuelven sin el backslash
                default -> "" + currentChar;
            };
        }
        return returnString;
    }


    /**
     * Avanza la posición de la location
     */
    private void consumePosition() {
        location.increaseColumn();
        location.increasePosition();
    }

    /**
     * @return Si se llegó al final del archivo
     */
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
        return chars[location.getPosition()];
    }

    /**
     * @return El siguiente caracter, sin consumir
     * @throws ArrayIndexOutOfBoundsException Si se llegó al final del archivo
     */
    private char peekNextChar() throws ArrayIndexOutOfBoundsException {
        return chars[location.getPosition() + 1];
    }


    /**
     * @return Si el siguiente token es un comentario
     * Observa el siguiente caracter y verifica si es el inicio de un comentario
     */
    private boolean isCommentStart() {
        if (!isEndOfFile()) {
            char currentChar = getCurrentChar();
            if (currentChar == '/') {
                try {
                    char nextChar = peekNextChar();
                    return nextChar == '?';
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * Ignora todos los caracteres hasta encontrar un salto de linea
     */
    private void removeComments() {
        while (!isEndOfFile() && getCurrentChar() != '\n') {
            consumePosition();
        }
    }
}
