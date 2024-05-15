package semantic.abstractSintaxTree;

import semantic.JsonHelper;

import java.util.HashMap;
import java.util.Map;

public class AbstractSyntaxTree {
    private Map<String, AstClassEntry> classes;

    private AstMethodEntry start;

    public AbstractSyntaxTree() {
        this.classes = new HashMap<>();
    }

    public Map<String, AstClassEntry> getClasses() {
        return classes;
    }

    public AstClassEntry getClass(String name) {
        return classes.get(name);
    }

    public void addClass(AstClassEntry newClass) {
        classes.put(newClass.getName(), newClass);
    }

    public void setStart(AstMethodEntry start) {
        this.start = start;
    }

    public AstMethodEntry getStart() {
        return start;
    }

    public String toJson() {
        int indentationIndex = 1;

        return "{" +
                JsonHelper.json("classes", this.classes, indentationIndex) +
                JsonHelper.json("start", this.start, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
