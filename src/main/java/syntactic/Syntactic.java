package syntactic;

import exceptions.lexical.LexicalException;
import exceptions.syntactic.SyntacticException;

public interface Syntactic {
    void analyze() throws LexicalException, SyntacticException;
}
