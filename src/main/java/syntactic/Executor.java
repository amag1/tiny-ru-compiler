package syntactic;

import reader.*;
import logger.*;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) {
        Reader reader;

        try {
            reader = new FileReader("/home/andres/IdeaProjects/tiny-ru-compiler/src/main/java/semtantic/symbolTable/test/failing/redefinedAttribute.ru");
            SyntacticExecutor executor = new SyntacticExecutor(reader, new ConsoleLogger());
            executor.execute();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
        }


    }
}
