import exceptions.lexical.LexicalException;
import lexical.Executor;
import lexical.Lexical;
import lexical.LexicalAnalyzer;
import lexical.Token;
import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;
import reader.FileReader;
import reader.StringReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConsoleLogger log = new ConsoleLogger();

        // Return error if no args
        if (args.length < 1) {
            log.LogText("No se especificó ningún archivo fuente");
            return;
        }

        // Get the file of source code
        String filePath = args[0];
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            log.LogText("No se encontró el archivo: \n" + filePath);
            return;
        }

        // Get the token
        Lexical lexical = new LexicalAnalyzer(fileReader);
        Executor executor = new Executor(lexical);
        List<Token> tokens;
        try {
            tokens = executor.getTokens();
        } catch (LexicalException e) {
            // Log the error
            log.LogLexicError(e);
            return;
        }

        Logger outputLogger;
        if (args.length > 1) {
            // Save the tokens in output file
            String outputFilePath = args[1];
            outputLogger = new FileLogger(outputFilePath);
        } else {
            // Logs the result in console
            outputLogger = log;
        }

        outputLogger.LogLexicSuccess(tokens);
    }
}
