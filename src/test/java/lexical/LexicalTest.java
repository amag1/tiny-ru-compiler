package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import org.junit.jupiter.api.Test;
import reader.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LexicalTest {
    @Test
    public void testClassNameStringSuccess() {
        Lexical lexical = new LexicalAnalyzer(new StringReader("Class"));
        try {
            Token token = lexical.nextToken();
            assertEquals("Class", token.getLexem());
            assertEquals(Type.ID_CLASS, token.getType());
            assertEquals(1, token.getLine());
            assertEquals(5, token.getColumn());
        } catch (LexicalException e) {
            fail();
        }
    }

    @Test
    public void testClassNameStringException() {
        Lexical lexical = new LexicalAnalyzer(new StringReader("Class3"));
        try {
            Token token = lexical.nextToken();
            fail();
        } catch (LexicalException e) {
            assertEquals(e.getClass(), MalformedClassIdentifierException.class);
        }
    }
}
