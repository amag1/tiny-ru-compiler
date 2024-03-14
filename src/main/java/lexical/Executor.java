package lexical;

import exceptions.lexical.LexicalException;
import reader.StringReader;
import logger.ConsoleLogger;
import logger.Logger;

import java.util.List;
import java.util.ArrayList;
public class Executor {
    public static void main(String[] args) {
        Lexical lexical = new LexicalAnalyzer(new StringReader("/? hola\n\n hlasn;as /?"));
        Logger log = new ConsoleLogger();
        try {
            List<Token> tokens = new ArrayList<Token>();
            if (!lexical.isEndOfFile()) {
                Token token = lexical.nextToken();
                while (token != null) {
                    tokens.add(token);
                    token = lexical.nextToken();

                }
            }
            log.LogLexicSuccess(tokens);
        } catch (LexicalException e) {
            log.LogLexicError(e);
        }
    }
}
