package semantic;

import exceptions.lexical.LexicalException;
import exceptions.semantic.SemanticException;
import exceptions.syntactic.SyntacticException;
import executor.Executor;
import lexical.LexicalAnalyzer;
import logger.ConsoleLogger;
import logger.Logger;
import reader.Reader;
import semantic.abstractSintaxTree.TinyRuAstHandler;
import semantic.symbolTable.SymbolTableHandler;
import semantic.symbolTable.TinyRuSymbolTableHandler;
import syntactic.Syntactic;
import syntactic.SyntacticAnalyzer;

/**
 * Implementación concreta de executor para analizador semántico
 */
public class SemanticExecutor extends Executor {

    private final Syntactic syntacticAnalyzer;

    public SemanticExecutor(Reader reader, Logger logger) {
        super(reader, logger);
        SymbolTableHandler stHandler = new TinyRuSymbolTableHandler();
        this.syntacticAnalyzer = new SyntacticAnalyzer(new LexicalAnalyzer(reader), stHandler, new TinyRuAstHandler(stHandler));
    }

    public void execute() {
        // Por ahora solo usa el console logger sin importar que le inyectan
        ConsoleLogger clogger = new ConsoleLogger();
        try {
            syntacticAnalyzer.analyze();
            String symbolTableJson = syntacticAnalyzer.getSymbolTableJson();
            clogger.LogSemanticSymbolTableSuccess();
            logger.LogSymbolTable(symbolTableJson);
        } catch (LexicalException e) {
            clogger.LogLexicError(e);
        } catch (SyntacticException e) {
            clogger.LogSyntacticError(e);
        } catch (SemanticException e) {
            clogger.LogSemanticError(e);
        }
    }
}
