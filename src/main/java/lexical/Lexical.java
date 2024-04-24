package lexical;

import exceptions.lexical.LexicalException;

/**
 * Interfaz para analizador sintáctico con métodos para obtener el siguiente token
 */
public interface Lexical {
    Token nextToken() throws LexicalException;

    int getColumn();

    int getLine();
}
