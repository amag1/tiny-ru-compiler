package semantic.abstractSintaxTree;

import java.util.HashMap;
import java.util.Map;

public class AbstractSyntaxTree {
    private Map<String, AstClassEntry> classes;

    private AstMethodEntry start;

    public  AbstractSyntaxTree() {
        this.classes = new HashMap<>();
        this.start = new AstMethodEntry("start");
    }

    public Map<String, AstClassEntry> getClasses() {
        return classes;
    }

    public AstClassEntry getClass(String name) {return classes.get(name);}

    public void addClass(AstClassEntry newClass) {classes.put(newClass.getName(), newClass);}

    public AstMethodEntry getStart() {
        return start;
    }
}
