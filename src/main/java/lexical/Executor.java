package lexical;

import exceptions.lexical.LexicalException;
import reader.StringReader;
import logger.ConsoleLogger;
import logger.Logger;

import java.util.List;
import java.util.ArrayList;
public class Executor {
    public static void main(String[] args) {

        Lexical lexical = new LexicalAnalyzer(new StringReader("\"hola\""));
        Logger log = new ConsoleLogger();
        try {
            List<Token> tokens = new ArrayList<>();
            while (!lexical.isEndOfFile()) {
                tokens.add(lexical.nextToken());
            }

            log.LogLexicSuccess(tokens);
        } catch (LexicalException e) {
            log.LogLexicError(e);
        }
    }
}
