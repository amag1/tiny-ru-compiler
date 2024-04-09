package logger;

import exceptions.lexical.LexicalException;
import exceptions.syntactic.SyntacticException;
import lexical.Token;

import java.util.List;

/**
 * Implementación de Logger para imprimir por consola.
 */
public class ConsoleLogger extends Logger {
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

    public void LogSyntacticSuccess() {
        List<String> message = GetSyntacticSuccessMessage();
        for (String line : message) {
            System.out.println(line);
        }
    }

    public void LogSyntacticError(SyntacticException e) {
        List<String> message = GetSyntacticErrorMessage(e);
        for (String line : message) {
            System.out.println(line);
        }
    }
}
