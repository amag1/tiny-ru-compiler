package lexical;

import exceptions.lexical.LexicalException;

public interface Lexical {
    public Token nextToken() throws LexicalException;
    public int getColumn();
    public int getLine();
}
