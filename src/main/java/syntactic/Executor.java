package syntactic;

import reader.*;
import logger.*;

public class Executor {
    public static void main(String[] args) {
        Reader reader = new StringReader("struct Test:Test { Array Int hola; Bool capo; Test capo; }struct Test:Test { Array Int hola; Bool capo; Test capo; } impl");
        SyntacticExecutor executor = new SyntacticExecutor(reader, new ConsoleLogger());
        executor.execute();
    }
}
