import exceptions.lexical.LexicalException;
import lexical.Executor;
import lexical.Lexical;
import lexical.LexicalAnalyzer;
import lexical.Token;
import logger.ConsoleLogger;
import logger.Logger;
import reader.FileReader;
import reader.StringReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Logger log = new ConsoleLogger();

        // Return error if no args
        if (args.length < 1) {
            log.LogText("No se especificó ningún archivo fuente");
            return;
        }

        // Get the file of source code
        String filePath = args[1];
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

        if (args.length > 2) {
            // Save the tokens in output file TODO
        } else {
            // Print the tokens in console
            log.LogLexicSuccess(tokens);
        }
    }
}
