import codeGeneration.CodeGenerator;
import exceptions.lexical.LexicalException;
import exceptions.semantic.symbolTable.SymbolTableException;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.syntactic.SyntacticException;
import executor.Executor;
import lexical.LexicalAnalyzer;
import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;
import reader.FileReader;
import semantic.SemanticExecutor;
import semantic.abstractSintaxTree.TinyRuAstHandler;
import semantic.symbolTable.SymbolTableHandler;
import semantic.symbolTable.TinyRuSymbolTableHandler;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticExecutor;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        // Return error if no args
        if (args.length < 1) {
            System.out.println("No se especificó ningún archivo fuente");
            return;
        }

        // Get the file of source code
        String filePath = args[0];
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo: \n" + filePath);
            return;
        }


        SymbolTableHandler stHandler = new TinyRuSymbolTableHandler();
        SyntacticAnalyzer analyzer = new SyntacticAnalyzer(new LexicalAnalyzer(fileReader), stHandler, new TinyRuAstHandler(stHandler.getSymbolTableLookup()));

        ConsoleLogger clogger = new ConsoleLogger();
        try {
            CodeGenerator codegen = analyzer.analyze();
            String code = codegen.generateCode();

            // Generar codigo en archivo de salida
            // Cambiar el .ru por .asm
            String outputPath = filePath.replace(".ru", ".asm");
            Logger logger = new FileLogger(outputPath);
            logger.Log(code);

            outputPath = filePath.replace(".ru", ".st.json");
            logger = new FileLogger(outputPath);
            logger.LogSymbolTable(codegen.getSymbolTable().toJson(0));

            outputPath = filePath.replace(".ru", ".ast.json");
            logger = new FileLogger(outputPath);
            logger.LogAst(codegen.getAst().toJson());

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
