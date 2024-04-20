package syntactic;

import exceptions.lexical.LexicalException;
import exceptions.semantic.SemanticException;
import exceptions.syntactic.SyntacticException;
import executor.Executor;
import lexical.LexicalAnalyzer;
import logger.ConsoleLogger;
import logger.Logger;
import reader.Reader;

/**
 * Implementación concreta de executor para analizador sintáctico
 * Implementa el método abstracto `execute`
 */
public class SyntacticExecutor extends Executor {

    private final Syntactic syntacticAnalyzer;

    public SyntacticExecutor(Reader reader, Logger logger) {
        super(reader, logger);
        this.syntacticAnalyzer = new SyntacticAnalyzer(new LexicalAnalyzer(reader));
    }

    public void execute() {
        // Por ahora solo usa el console logger sin importar que le inyectan
        ConsoleLogger clogger = new ConsoleLogger();
        try {
            syntacticAnalyzer.analyze();
            clogger.LogSyntacticSuccess();
        } catch (LexicalException e) {
            clogger.LogLexicError(e);
        } catch (SyntacticException e) {
            clogger.LogSyntacticError(e);
        } catch (SemanticException e) {
            clogger.LogSemanticError(e);
        }
    }
}
