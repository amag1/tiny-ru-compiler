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
            // Símbolos simples
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

            // Operadores aritméticos
            case '+' -> matchPlusSign(startLocation);
            case '-' -> matchMinusSign(startLocation);
            case '=' -> matchEqualSign(startLocation);
            case '>' -> matchGreaterThan(startLocation);
            case '<' -> matchLessSign(startLocation);
            case '!' -> matchNotEqual(startLocation);

            // Literales
            case '\'' -> matchCharLiteral(startLocation);
            case '\"' -> matchStringLiteral(startLocation);

            // Operadores lógicos
            case '|', '&' -> matchTwoSymbolsOrFail(startLocation, currentChar);

            // Por defecto, matchea un string complejo
            default -> matchComplexString(startLocation);
        };

        if (token.getType() == null) {
            throw new InvalidCharacterException(currentChar, startLocation);
        }

        return token;
    }

    /**
     * @param startLocation La posición inicial del token
     * @param firstChar     El primer caracter esperado del token
     * @param secondChar    El segundo caracter esperado del token
     * @return Un token de uno o dos caracteres.
     */
    private Token matchOneOrTwoCharToken(Location startLocation, char firstChar, char secondChar) {
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

    private Token matchNotEqual(Location startLocation) {
        return matchOneOrTwoCharToken(startLocation, '!', '=');
    }

    private Token matchGreaterThan(Location startLocation) {
        return matchOneOrTwoCharToken(startLocation, '>', '=');
    }

    private Token matchLessSign(Location startLocation) {
        return matchOneOrTwoCharToken(startLocation, '<', '=');
    }

    private Token matchEqualSign(Location startLocation) {
        return matchOneOrTwoCharToken(startLocation, '=', '=');
    }

    private Token matchPlusSign(Location startLocation) {
        return matchOneOrTwoCharToken(startLocation, '+', '+');
    }

    private Token matchMinusSign(Location startLocation) {
        // Intenta matchear con el token flecha
        try {
            if (peekNextChar() == '>') {
                consumePosition();
                consumePosition();
                return new Token("->", Type.ARROW, startLocation);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        // Si no lo consigue, matchea con el token menos
        return matchOneOrTwoCharToken(startLocation, '-', '-');
    }


    /**
     * @param location    La posición inicial del token
     * @param charToMatch El caracter que se espera matchear
     * @return Un token de dos caracteres
     * @throws MalformedDoubleSymbolException Si no se matchea el token
     */
    private Token matchTwoSymbolsOrFail(Location location, char charToMatch) throws MalformedDoubleSymbolException {
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
                return new Token(lexeme, PredefinedLexemeMap.getType(lexeme), location);
            }
        }

        throw new MalformedDoubleSymbolException(charToMatch, location);
    }

    /**
     * @param startLocation La posición inicial del token
     * @return Un token de tipo STRING_LITERAL
     * @throws UnclosedStringLiteralException  Si se llega al final del archivo sin cerrar el string
     * @throws MalformedStringLiteralException Error genérico al matchear el string
     * @throws InvalidCharacterException       Si se encuentra un caracter inválido
     * @throws StringLiteralTooLongException   Si el string supera los 1024 caracteres de longitud
     */
    private Token matchStringLiteral(Location startLocation) throws UnclosedStringLiteralException, MalformedStringLiteralException, InvalidCharacterException, StringLiteralTooLongException {
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

    /**
     * @param startLocation La posición inicial del token
     * @return Un token de tipo CHAR_LITERAL
     * @throws MalformedCharLiteralException Si el char no es válido
     * @throws UnclosedCharLiteralException  Si se llega al final del archivo sin cerrar el char
     * @throws InvalidCharacterException     Si se encuentra un caracter inválido
     * @throws EmptyCharLiteralException     Si el char está vacío
     */
    private Token matchCharLiteral(Location startLocation) throws MalformedCharLiteralException, UnclosedCharLiteralException, InvalidCharacterException, EmptyCharLiteralException {
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
            if (CharUtils.isValidChar(currentChar)) {
                lexeme += currentChar;
                consumePosition();
            }
            else {
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

    /**
     * @param startLocation La posición inicial del token
     * @return Un token de tipo ID_CLASS, ID o INT_LITERAL
     * @throws LexicalException Si se encuentra un error al matchear el token
     *                          Esta funcion se llama en el caso default del switch, y matchea un string complejo,
     *                          que puede ser un ID_CLASS, ID o INT_LITERAL.
     */
    private Token matchComplexString(Location startLocation) throws LexicalException {
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
