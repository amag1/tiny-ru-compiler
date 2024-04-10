package syntactic;

import reader.*;
import logger.*;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) {
        Reader reader = new StringReader("start { if () {}}");

        //try {

            //Reader reader = new FileReader("/home/andres/IdeaProjects/tiny-ru-compiler/src/main/java/syntactic/test/passing/all.ru");
            SyntacticExecutor executor = new SyntacticExecutor(reader, new ConsoleLogger());
            executor.execute();
        //} catch (FileNotFoundException e) {
          //  throw new RuntimeException(e);
        //}
    }
}
