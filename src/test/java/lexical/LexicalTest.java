package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reader.FileReader;
import reader.StringReader;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("provideStringsForClassName")
    public void testFailingLexer(String input, Class<? extends LexicalException> error) throws FileNotFoundException {
        Lexical lexical = new LexicalAnalyzer(new FileReader(input));
        try {
            Token token = lexical.nextToken();
            fail();
        } catch (LexicalException e) {
            assertEquals(e.getClass(), error);
        }
    }

    private static Stream<Arguments> provideStringsForClassName() {
        String basepath = "src/main/java/lexical/test/";
        return Stream.of(
            Arguments.of(basepath + "00.ru", MalformedClassIdentifierException.class),
                Arguments.of(basepath + "01.ru", MalformedClassIdentifierException.class)
        );
    }
}