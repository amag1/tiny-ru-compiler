package logger;

import exceptions.lexical.LexicalException;
import lexical.Token;

import java.util.List;

public class ConsoleLogger extends Logger{
    public ConsoleLogger() {
        super();
    }

    public void LogLexicSuccess(List<Token> tokens) {
        List<String> message = GetLexicSuccessMessage(tokens);
        for (String line : message) {
            System.out.println(line);
        }
    }

    public void LogLexicError(LexicalException e) {
        List<String> message = GetLexicErrorMessage(e);
        for (String line : message) {
            System.out.println(line);
        }
    }
}
