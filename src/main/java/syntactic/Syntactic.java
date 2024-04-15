package syntactic;

import exceptions.lexical.LexicalException;
import exceptions.syntactic.SyntacticException;

/**
 * Interfaz para analizadores sintácticos.
 */
public interface Syntactic {
    void analyze() throws LexicalException, SyntacticException;
}
