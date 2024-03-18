package lexical;

import exceptions.lexical.*;
import location.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reader.FileReader;

import java.io.FileNotFoundException;
import java.io.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LexicalTest {
    @Test
    public void testClassNameStringSuccess() {
        String codeText = "Class";
        Token[] expectedTokens = {
                new Token("Class", Type.ID_CLASS, new Location(1, 1)),
        };

        LexicalTestTokenSuccess test = new LexicalTestTokenSuccess(codeText, expectedTokens);

        test.run();
    }

    @Test
    public void MultipleIdsSuccess() {
        String codeText = "pepe meshna";
        Token[] expectedTokens = {
                new Token("pepe", Type.ID, new Location(1, 1)),
                new Token("meshna", Type.ID, new Location(1, 6)),
        };

        LexicalTestTokenSuccess test = new LexicalTestTokenSuccess(codeText, expectedTokens);

        test.run();
    }

    @Test
    public void testClassNameStringException() {
        LexicalException expectedError = new MalformedClassIdentifierException("", new Location(1, 7));
        LexicalTestTokenFailure test = new LexicalTestTokenFailure("Class3", expectedError);

        test.run();
    }

    @ParameterizedTest
    @MethodSource("providePassingLexer")
    public void testPassingLexer(String input) {
        try {
            Lexical lexical = new LexicalAnalyzer(new FileReader(input));

            Token token = lexical.nextToken();
            while (token != null) {
                token = lexical.nextToken();
            }

            fail("Se esperaba una excepción en input " + input);

        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado: " + input);
        } catch (LexicalException e) {
            fail("Error en input: " + input + ", " + e.getMessage());
        }
    }


    private static Stream<String> providePassingLexer() {
        String basepath = "src/main/java/lexical/test/passing";
        File dir = new File(basepath);
        if (!dir.exists()) {
            fail("Directorio no encontrado: " + basepath);
        }

        File[] listing = dir.listFiles();
        if (listing == null) {
            fail("No se encontraron archivos en el directorio: " + basepath);
        }

        Stream.Builder<String> builder = Stream.builder();
        for (File child : listing) {
            builder.add(child.getPath());
        }

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("provideFailingLexer")
    public void testFailingLexer(String input) {
        try {
            Lexical lexical = new LexicalAnalyzer(new FileReader(input));
            try {
                Token token = lexical.nextToken();
                while (token != null) {
                    token = lexical.nextToken();
                }
                fail("Se esperaba una excepción en input " + input);
            } catch (LexicalException e) {
                // test passed
            }
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado: " + input);
        }
    }

    private static Stream<String> provideFailingLexer() {
        String basepath = "src/main/java/lexical/test/failing";
        File dir = new File(basepath);
        if (!dir.exists()) {
            fail("Directorio no encontrado: " + basepath);
        }

        File[] listing = dir.listFiles();
        if (listing == null) {
            fail("No se encontraron archivos en el directorio: " + basepath);
        }

        Stream.Builder<String> builder = Stream.builder();
        for (File child : listing) {
            builder.add(child.getPath());
        }

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("provideStringsForClassName")
    public void testFailingLexerWithErrorAssertion(String input, Class<? extends LexicalException> error) throws FileNotFoundException {
        Lexical lexical = new LexicalAnalyzer(new FileReader(input));
        try {
            Token token = lexical.nextToken();
            while (token != null) {
                token = lexical.nextToken();
            }
            fail("Se esperaba una excepción en input " + input);
        } catch (LexicalException e) {
            assertEquals(e.getClass(), error, "Error en input: " + input + ", se esperaba: " + e.getClass());
        }
    }

    private static Stream<Arguments> provideStringsForClassName() {
        String basepath = "src/main/java/lexical/test/failing/";
        return Stream.of(
                Arguments.of(basepath + "00.ru", MalformedClassIdentifierException.class),
                Arguments.of(basepath + "01.ru", InvalidCharacterException.class),
                Arguments.of(basepath + "02.ru", InvalidCharacterException.class),
                Arguments.of(basepath + "03.ru", MalformedIntLiteralException.class),
                Arguments.of(basepath + "04.ru", InvalidCharacterException.class),
                Arguments.of(basepath + "05.ru", UnclosedStringLiteralException.class),
                Arguments.of(basepath + "06.ru", MalformedCharLiteralException.class),
                Arguments.of(basepath + "07.ru", EmptyCharLiteralException.class),
                Arguments.of(basepath + "08.ru", UnclosedCharLiteralException.class),
                Arguments.of(basepath + "09.ru", InvalidCharacterException.class),
                Arguments.of(basepath + "10.ru", UnclosedStringLiteralException.class),
                Arguments.of(basepath + "11.ru", UnclosedStringLiteralException.class),
                Arguments.of(basepath + "12.ru", MalformedStringLiteralException.class),
                Arguments.of(basepath + "13.ru", StringLiteralTooLongException.class)
        );
    }
}