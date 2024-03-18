import exceptions.lexical.LexicalException;
import executor.Executor;
import lexical.LexicalExecutor;
import lexical.Lexical;
import lexical.LexicalAnalyzer;
import lexical.Token;
import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;
import reader.FileReader;

import java.io.FileNotFoundException;
import java.util.List;

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

        // Get the output logger;
        Logger outputLogger;
        if (args.length > 1) {
            // Save the tokens in output file
            String outputFilePath = args[1];
            outputLogger = new FileLogger(outputFilePath);
        } else {
            // Logs the result in console
            outputLogger = new ConsoleLogger();;
        }

        // Execute
        Executor executor = new LexicalExecutor(fileReader, outputLogger);
        executor.execute();
    }
}
