package logger;

import exceptions.lexical.LexicalException;
import exceptions.semantic.SemanticException;
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

    public void LogSemanticSymbolTableSuccess() {
        List<String> message = GetSemanticSymbolTableSuccessMessage();
        for (String line : message) {
            System.out.println(line);
        }
    }

    public void LogSemanticError(SemanticException e) {
        List<String> message = GetSemanticErrorMessage(e);
        for (String line : message) {
            System.out.println(line);
        }
    }

    public void LogSymbolTable(String symbolTableJson) {
        System.out.println(symbolTableJson);
    }
}
