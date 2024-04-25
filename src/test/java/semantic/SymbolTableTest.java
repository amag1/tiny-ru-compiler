package semantic;

import exceptions.semantic.SemanticException;
import lexical.LexicalAnalyzer;
import logger.ConsoleLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reader.FileReader;
import semantic.symbolTable.DummySymbolTableHandler;
import semantic.symbolTable.TinyRuSymbolTableHandler;
import syntactic.Syntactic;
import syntactic.SyntacticAnalyzer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class SymbolTableTest {
    private static Stream<Arguments> provider(String basepath) {
        File dir = new File(basepath);
        if (!dir.exists()) {
            Assertions.fail("Directorio no encontrado: " + basepath);
        }

        File[] listing = dir.listFiles();
        if (listing == null) {
            Assertions.fail("No se encontraron archivos en el directorio: " + basepath);
        }

        Stream.Builder<Arguments> builder = Stream.builder();
        // Look for .ru and their matching .json files
        for (File child : listing) {
            if (child.getName().endsWith(".ru")) {
                String jsonPath = child.getPath().replace(".ru", ".json");
                builder.add(Arguments.of(child.getPath(), jsonPath));
            }
        }

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("providePassingSymbolTable")
    public void TestPassingSymbolTable(String input, String fileOutput) {
        try {
            Syntactic syntactic = new SyntacticAnalyzer(new LexicalAnalyzer(new FileReader(input)), new TinyRuSymbolTableHandler());
            syntactic.analyze();
            String st = syntactic.getSymbolTableJson();

            // Open the file and compare it with the output
            Path filepath = Path.of(fileOutput);
            String content = Files.readString(filepath);

            // Compare the content
            if (!JsonComparer.compare(content, st)) {
                fail("El archivo: " + input + " no coincide con el archivo de salida");
            }
        } catch (SemanticException e) {
            ConsoleLogger clog = new ConsoleLogger();
            clog.LogSemanticError(e);
            fail();
        } catch (Exception e) {
            fail("Error en el archivo: " + input + "\n" + e.getMessage());
        }
    }

    private static Stream<Arguments> providePassingSymbolTable() {
        String basepath = "src/main/java/semantic/symbolTable/test/passing";
        return provider(basepath);
    }
}
