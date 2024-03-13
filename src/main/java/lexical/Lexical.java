package lexical;

import exceptions.lexical.LexicalException;

public interface Lexical {
    public Token nextToken() throws LexicalException;
    public boolean isEndOfFile();
    public int getColumn();
    public int getLine();
    public void removeWhitespaces();
    public void removeComments() throws LexicalException;

}
