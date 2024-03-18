package logger;

import lexical.Token;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileLogger extends Logger{

    File file;

    public FileLogger(String path){
        this.file = new File(path);
        try {
            this.file.createNewFile();
        } catch (IOException e) {
            System.out.println("Ocurrió un error al obtener el archivo: \n" + path);
        }
    }

    public  void LogLexicSuccess(List<Token> tokens) {
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
}
