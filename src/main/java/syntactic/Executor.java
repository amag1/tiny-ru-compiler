package syntactic;

import reader.*;
import logger.*;

import java.io.FileNotFoundException;

public class Executor {
    public static void main(String[] args) {
        Reader reader;

        reader = new StringReader("struct A{ } struct A{} start{}");


        SyntacticExecutor executor = new SyntacticExecutor(reader, new ConsoleLogger());
        executor.execute();
    }
}
