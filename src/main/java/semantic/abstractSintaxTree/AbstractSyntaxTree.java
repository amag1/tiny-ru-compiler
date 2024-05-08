package semantic.abstractSintaxTree;

import java.util.Map;

public class AbstractSyntaxTree {
    private Map<String, AstClassEntry> classes;

    private AstMethodEntry start;

    public Map<String, AstClassEntry> getClasses() {
        return classes;
    }
}
