package syntactic;

import reader.*;
import logger.*;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) {
        Reader reader;
//        reader = new StringReader("start { if (x > 5) {y = x; (hola(pepe.s.x,x)); (Xasd.get()); y = xasd.p.s.s.s.s.get(); (xasd.hola()); b = new Int[5]; b = new A();}}");
        reader = new StringReader("start {b = new Int[5];}");
        /*
        try {
            reader = new FileReader("/home/andres/IdeaProjects/tiny-ru-compiler/src/main/java/syntactic/test/passing/all.ru");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
         */

        SyntacticExecutor executor = new SyntacticExecutor(reader, new ConsoleLogger());
        executor.execute();
    }
}
