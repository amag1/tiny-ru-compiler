package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import location.Location;
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
        String codeText = "Cla@ss";
        Token[] expectedTokens = {
                new Token("Class", Type.ID_CLASS, new Location(1, 0)),
        };

        LexicalTestTokenSuccess test = new LexicalTestTokenSuccess(codeText, expectedTokens);

        test.run();
    }

    @Test
    public void MultipleIdsSuccess() {
        String codeText = "pepe meshna";
        Token[] expectedTokens = {
                new Token("pepe", Type.ID, new Location(1, 4)),
                new Token("meshna", Type.ID, new Location(1, 11)),
        };

        LexicalTestTokenSuccess test = new LexicalTestTokenSuccess(codeText, expectedTokens);

        test.run();
    }

    @Test
    public void testClassNameStringException() {
        LexicalException expectedError =   new MalformedClassIdentifierException("", new Location(1, 6));
        LexicalTestTokenFailure test = new LexicalTestTokenFailure("Class3", expectedError);

        test.run();
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