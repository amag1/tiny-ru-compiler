package lexical;

import exceptions.lexical.LexicalException;

/**
 * Interfaz para el analizador léxico.
 */
public interface Lexical {
    /**
     * @return el siguiente token del archivo
     * @throws LexicalException una excepción léxica detallando el error.
     */
    public Token nextToken() throws LexicalException;

    public boolean isEndOfFile();

    public int getColumn();

    public int getLine();
}
