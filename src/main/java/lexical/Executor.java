package lexical;

import exceptions.lexical.LexicalException;
import reader.StringReader;
import logger.ConsoleLogger;
import logger.Logger;

import java.util.List;
import java.util.ArrayList;
public class Executor {
    public static void main(String[] args) {
        Lexical lexical = new LexicalAnalyzer(new StringReader("\"'\""));
        Logger log = new ConsoleLogger();
        try {
            List<Token> tokens = new ArrayList<Token>();
            while (!lexical.isEndOfFile()) {
                tokens.add(lexical.nextToken());
            }

            log.LogLexicSuccess(tokens);
        } catch (LexicalException e) {
            log.LogLexicError(e);
        }
    }
}
