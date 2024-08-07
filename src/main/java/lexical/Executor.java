package lexical;

import exceptions.lexical.LexicalException;
import reader.FileReader;
import logger.ConsoleLogger;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class Executor {
    public static void main(String[] args) {
        ConsoleLogger log = new ConsoleLogger();
        try {
            Lexical lexical = new LexicalAnalyzer(new FileReader("/home/andres/IdeaProjects/tiny-ru-compiler/src/main/java/lexical/test/passing/fibonacci.ru"));


            List<Token> tokens = new ArrayList<Token>();

            Token token = lexical.nextToken();
            while (token != null) {
                tokens.add(token);
                token = lexical.nextToken();
            }

            log.LogLexicSuccess(tokens);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (LexicalException e) {
            log.LogLexicError(e);
        }
    }
}
