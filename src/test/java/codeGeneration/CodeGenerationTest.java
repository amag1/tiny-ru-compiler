package codeGeneration;
import exceptions.lexical.*;
import exceptions.semantic.symbolTable.SymbolTableException;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.syntactic.SyntacticException;
import executor.Executor;
import lexical.Lexical;
import lexical.LexicalAnalyzer;
import lexical.Token;
import location.Location;
import logger.ConsoleLogger;
import logger.FileLogger;
import logger.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reader.FileReader;
import semantic.SemanticExecutor;
import semantic.abstractSintaxTree.TinyRuAstHandler;
import semantic.symbolTable.SymbolTableHandler;
import semantic.symbolTable.TinyRuSymbolTableHandler;
import syntactic.Syntactic;
import syntactic.SyntacticAnalyzer;
import syntactic.SyntacticExecutor;

import java.io.FileNotFoundException;
import java.io.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class CodeGenerationTest {
    private static final String MARS_PATH = "Mars4_5.jar";
    private static final String DIRECTORY = "src/main/java/codeGeneration/test";
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

    public static Stream<Arguments> provideCodeGenTests() {
        return provider(DIRECTORY+ "/passing");
    }

    public static Stream<Arguments> provideFailingCodeGenTests() {
        return provider(DIRECTORY+ "/failing");
    }


    @ParameterizedTest
    @MethodSource("provideCodeGenTests")
    public void testPassingTests(String input) throws FileNotFoundException {
        String fileName = input;
        compile(fileName);
        String output = run(fileName);
        String expected = getExpectedOutput(fileName);
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @MethodSource("provideFailingCodeGenTests")
    public void testFailingTests(String input) throws FileNotFoundException {
        String fileName = input;
        compile(fileName);
        String output = run(fileName);
        String expected = getExpectedOutput(fileName);
        assertEquals(expected, output);
    }


    private static void compile(String fileName) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(fileName);
        } catch (IOException e) {
            return;
        }
        SymbolTableHandler stHandler = new TinyRuSymbolTableHandler();
        SyntacticAnalyzer analyzer = new SyntacticAnalyzer(new LexicalAnalyzer(fileReader), stHandler, new TinyRuAstHandler(stHandler.getSymbolTableLookup()));

        ConsoleLogger clogger = new ConsoleLogger();
        try {
            CodeGenerator codegen = analyzer.analyze();
            String code = codegen.generateCode(true);

            // Generar codigo en archivo de salida
            // Cambiar el .ru por .asm
            String outputPath = fileName.replace(".ru", ".asm");
            Logger logger = new FileLogger(outputPath);
            logger.Log(code);

            outputPath = fileName.replace(".ru", ".st.json");
            logger = new FileLogger(outputPath);
            logger.LogSymbolTable(codegen.getSymbolTable().toJson(0));

            outputPath = fileName.replace(".ru", ".ast.json");
            logger = new FileLogger(outputPath);
            logger.LogAst(codegen.getAst().toJson());

        } catch (LexicalException e) {
            clogger.LogLexicError(e);
        } catch (SyntacticException e) {
            clogger.LogSyntacticError(e);
        } catch (SymbolTableException e) {
            clogger.LogSymbolTableError(e);
        } catch (AstException e) {
            clogger.LogAstError(e);
        }
    }

    private static String run(String fileName) {
        String asmFileName = fileName.replace(".ru", ".asm");
        String output = "";
        try {
            Process process = new ProcessBuilder("java", "-jar", MARS_PATH, asmFileName, "nc").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static String getExpectedOutput(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String expectedOutput = "";
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("/? ")) {
                    expectedOutput += line.substring(3);
                } else if (line.startsWith("/?")) {
                    break;
                }
            }

            expectedOutput = expectedOutput.trim();
            return  expectedOutput;

        } catch (IOException e) {
            return "";
        }
    }

}