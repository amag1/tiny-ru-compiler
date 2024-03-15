package lexical;

import exceptions.lexical.LexicalException;
import logger.FileLogger;
import reader.FileReader;
import reader.StringReader;
import logger.ConsoleLogger;
import logger.Logger;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
public class Executor {
    public static void main(String[] args) {
        Logger log = new FileLogger("C:\\Users\\Usuario\\Documents\\Andrés\\Facu\\2024\\tiny-ru-compiler\\src\\main\\java\\lexical\\test\\passing\\text.txt");
        try {
            Lexical lexical = new LexicalAnalyzer(new FileReader("C:\\Users\\Usuario\\Documents\\Andrés\\Facu\\2024\\tiny-ru-compiler\\src\\main\\java\\lexical\\test\\passing\\01.ru"));


            List<Token> tokens = new ArrayList<Token>();
            if (!lexical.isEndOfFile()) {
                Token token = lexical.nextToken();
                while (token != null) {
                    tokens.add(token);
                    token = lexical.nextToken();

                }
            }
            log.LogLexicSuccess(tokens);
        } catch (FileNotFoundException e) {

        } catch (LexicalException e) {
            log.LogLexicError(e);
        }
    }
}
