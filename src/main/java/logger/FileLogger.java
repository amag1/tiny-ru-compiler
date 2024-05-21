package logger;

import lexical.Token;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Implementación de logger que permite escribir el éxito sintáctico en un nuevo archivo.
 */
public class FileLogger extends Logger {

    File file;
    String path;

    public FileLogger(String path) {
        this.path = path;
    }

    private void initializeFile(String path) {
        if (this.file != null) {
            return;
        }

        this.file = new File(path);
        try {
            this.file.createNewFile();
        } catch (IOException e) {
            System.out.println("Ocurrió un error al obtener el archivo: \n" + path);
        }
    }

    public void LogLexicSuccess(List<Token> tokens) {
        initializeFile(this.path);
        List<String> message = GetLexicSuccessMessage(tokens);

        try {
            FileWriter writer = new FileWriter(this.file);

            for (String line : message) {
                writer.write(line);
                writer.write("\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el archivo destino");
        }
    }

    public void LogSymbolTable(String symbolTableJson) {
        initializeFile(this.path);
        try {
            FileWriter writer = new FileWriter(this.file);
            writer.write(symbolTableJson);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el archivo destino");
        }
    }

    public void LogAst(String astJson) {
        initializeFile(this.path);
        try {
            FileWriter writer = new FileWriter(this.file);
            writer.write(astJson);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el archivo destino");
        }
    }
}
