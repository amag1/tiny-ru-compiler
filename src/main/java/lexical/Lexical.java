package lexical;

import exceptions.lexical.LexicalException;

/**
 * Interfaz para analizador sintáctico con métodos para obtener el siguiente token
 */
public interface Lexical {
    public Token nextToken() throws LexicalException;

    public int getColumn();

    public int getLine();
}
