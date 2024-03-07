package lexical;

import exceptions.lexical.LexicalException;
import reader.StringReader;

public class Executor {
    public static void main(String[] args) {
        Lexical lexical = new LexicalAnalyzer(new StringReader("Hola"));
        try {
            while (!lexical.ReachedEOF()) {
                Token token = lexical.nextToken();
                System.out.println(token.getLexem() + " " + token.getType());

            }
        } catch (LexicalException e) {
            System.out.println(e.getMessage());
        }
    }
}
