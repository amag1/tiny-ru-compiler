package lexical;

import exceptions.lexical.LexicalException;
import reader.StringReader;
import logger.ConsoleLogger;
import logger.Logger;

import java.util.List;
import java.util.ArrayList;
public class Executor {
    public static void main(String[] args) {
        String text = " /? hola \n hola ";
        Lexical lexical = new LexicalAnalyzer(new StringReader(text));
        Logger log = new ConsoleLogger();
        try {
            List<Token> tokens = new ArrayList<Token>();
            do {
                Token token = lexical.nextToken();

                if (token != null) {
                    tokens.add(token);
                }
            } while (!lexical.isEndOfFile());

            log.LogLexicSuccess(tokens);
        } catch (LexicalException e) {
            log.LogLexicError(e);
        }
    }
}
