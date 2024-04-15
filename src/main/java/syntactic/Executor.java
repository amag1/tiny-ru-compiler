package syntactic;

import reader.*;
import logger.*;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) {
        Reader reader;

        try {
            reader = new FileReader("/home/andres/IdeaProjects/tiny-ru-compiler/src/main/java/syntactic/test/passing/test.ru");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        SyntacticExecutor executor = new SyntacticExecutor(reader, new ConsoleLogger());
        executor.execute();
    }
}
