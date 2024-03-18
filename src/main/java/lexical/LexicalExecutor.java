package lexical;

import exceptions.lexical.LexicalException;
import executor.Executor;
import logger.ConsoleLogger;
import reader.Reader;
import logger.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class LexicalExecutor extends Executor {

    private Lexical lexical;

    public LexicalExecutor(Reader reader, Logger logger) {
        super(reader, logger);
        lexical = new LexicalAnalyzer(reader);
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

    public void execute() {
        List<Token> tokens;
        try {
            tokens = getTokens();
        } catch (LexicalException e) {
            // Log the error
            ConsoleLogger log = new ConsoleLogger();
            log.LogLexicError(e);
            return;
        }

        this.logger.LogLexicSuccess(tokens);
    }
}
