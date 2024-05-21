import executor.Executor;
import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;
import reader.FileReader;
import semantic.SemanticExecutor;
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

        // Get the output logger;

        // Generate path for output from input
        String astOutputFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".ast.json";
        String sTableOutputFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + ".st.json";
        Logger outputLogger = new FileLogger(astOutputFilePath);

        // Execute
        Executor executor = new SemanticExecutor(fileReader, outputLogger);
        executor.setBackupLogger(new FileLogger(sTableOutputFilePath));
        executor.execute();
    }
}
