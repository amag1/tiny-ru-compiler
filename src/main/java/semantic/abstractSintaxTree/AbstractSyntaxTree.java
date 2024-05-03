package semantic.abstractSintaxTree;

import java.util.Map;

public class AbstractSyntaxTree {
    private AstClassEntry[] classes;

    private AstMethodEntry start;

    public AstClassEntry[] getClasses() {
        return classes;
    }
}
