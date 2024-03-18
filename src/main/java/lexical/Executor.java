package lexical;

import exceptions.lexical.LexicalException;
import reader.FileReader;
import reader.StringReader;
import logger.ConsoleLogger;
import logger.Logger;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
public class Executor {

    private Lexical lexical;

    public Executor(Lexical lexical) {
        this.lexical = lexical;
    }

    public List<Token> getTokens() throws LexicalException {
        List<Token> tokens = new ArrayList<Token>();
        if (!lexical.isEndOfFile()) {
            Token token = lexical.nextToken();
            while (token != null) {
                tokens.add(token);
                token = lexical.nextToken();
            }
        }
        return tokens;
    }
}
