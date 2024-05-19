package semantic;

import exceptions.semantic.symbolTable.SymbolTableException;
import exceptions.semantic.syntaxTree.AstException;
import lexical.LexicalAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reader.FileReader;
import semantic.abstractSintaxTree.DummyAstHandler;
import semantic.abstractSintaxTree.TinyRuAstHandler;
import semantic.symbolTable.SymbolTableHandler;
import semantic.symbolTable.TinyRuSymbolTableHandler;
import syntactic.Syntactic;
import syntactic.SyntacticAnalyzer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AstTest {
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

    private static Stream<Arguments> provideFailingAst() {
        String basepath = "src/main/java/semantic/abstractSintaxTree/test/failing";
        return provider(basepath);
    }

    @ParameterizedTest
    @MethodSource("provideFailingAst")
    public void TestFailingAst(String input) {
        // Get the first line in the file
        String errorName = "";
        try {
            // Open file and check first line for error name
            String firstLine = Files.lines(Path.of(input)).findFirst().orElse("/?");
            // Get string after /? to get error name
            errorName = firstLine.substring(firstLine.indexOf("/?") + 2).trim();

            SymbolTableHandler stHandler = new TinyRuSymbolTableHandler();
            Syntactic syntactic = new SyntacticAnalyzer(new LexicalAnalyzer(new FileReader(input)), stHandler, new TinyRuAstHandler(stHandler.getSymbolTableLookup()));
            syntactic.analyze();

            fail("El archivo " + input + " no contiene errores");
        } catch (AstException e) {
            // Check that the class of the exception equals errorName
            assertEquals(e.getClass().getSimpleName(), errorName, "Error en el archivo: " + input);
        } catch (Exception e) {
            fail("Unexpected exception in " + input + ": " + e.getMessage());
        }
    }
}
