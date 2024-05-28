package semantic.abstractSintaxTree;

import codeGeneration.Generable;
import semantic.Json;

public abstract class AbstractSyntaxNode implements Json, Generable {
    protected String nodeType;

    public String generate() {
        return "";
    }
}
