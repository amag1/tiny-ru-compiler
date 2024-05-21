package semantic;

import exceptions.lexical.LexicalException;
import exceptions.semantic.symbolTable.SymbolTableException;
import exceptions.semantic.syntaxTree.AstException;
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
    private Logger secondLogger;

    public SemanticExecutor(Reader reader, Logger logger) {
        super(reader, logger);
        SymbolTableHandler stHandler = new TinyRuSymbolTableHandler();
        this.syntacticAnalyzer = new SyntacticAnalyzer(new LexicalAnalyzer(reader), stHandler, new TinyRuAstHandler(stHandler.getSymbolTableLookup()));
    }

    @Override
    public void setBackupLogger(Logger secondLogger) {
        this.secondLogger = secondLogger;
    }

    public void execute() {
        // Por ahora solo usa el console logger sin importar que le inyectan
        ConsoleLogger clogger = new ConsoleLogger();
        try {
            syntacticAnalyzer.analyze();
            String symbolTableJson = syntacticAnalyzer.getSymbolTableJson();
            String astJson = syntacticAnalyzer.getAbstractSybolTreeJson();
            logger.LogAst(astJson);
            secondLogger.LogSymbolTable(symbolTableJson);
            clogger.LogAstSuccess();

        } catch (LexicalException e) {
            clogger.LogLexicError(e);
        } catch (SyntacticException e) {
            clogger.LogSyntacticError(e);
        } catch (SymbolTableException e) {
            clogger.LogSymbolTableError(e);
        } catch (AstException e) {
            clogger.LogAstError(e);
        }
    }
}
