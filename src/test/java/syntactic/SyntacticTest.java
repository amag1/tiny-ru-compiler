package syntactic;

import exceptions.syntactic.SyntacticException;
import lexical.LexicalAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;

import reader.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class SyntacticTest {
    private static Stream<String> provider(String basepath) {
        File dir = new File(basepath);
        if (!dir.exists()) {
            Assertions.fail("Directorio no encontrado: " + basepath);
        }

        File[] listing = dir.listFiles();
        if (listing == null) {
            Assertions.fail("No se encontraron archivos en el directorio: " + basepath);
        }

        Stream.Builder<String> builder = Stream.builder();
        for (File child : listing) {
            builder.add(child.getPath());
        }

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("providePassingSyntactic")
    public void TestPassingSyntactic(String input) {
        try {
            Syntactic syntactic = new SyntacticAnalyzer(new LexicalAnalyzer(new FileReader(input)));
            syntactic.analyze();
        } catch (Exception e) {
            fail("Error en el archivo: " + input + "\n" + e.getMessage());
        }
    }

    private static Stream<String> providePassingSyntactic() {
        String basepath = "src/main/java/syntactic/test/passing";
        return provider(basepath);
    }

    @ParameterizedTest
    @MethodSource("provideFailingSyntactic")
    public void TestFailingSyntactic(String input) {
        try {
            Syntactic syntactic = new SyntacticAnalyzer(new LexicalAnalyzer(new FileReader(input)));
            try {
                syntactic.analyze();
                fail("Se esperaba una excepción en el archivo: " + input);
            } catch (SyntacticException e) {
                // test passed
            }
        } catch (Exception e) {
            fail("Error en el archivo: " + input + "\n" + e.getMessage());
        }
    }

    private static Stream<String> provideFailingSyntactic() {
        String basepath = "src/main/java/syntactic/test/failing";
        return provider(basepath);
    }
}
