package semantic.abstractSintaxTree;

import semantic.Json;

public abstract class AbstractSyntaxNode implements Json {
    protected String nodeType;

    public String generate(Context context, boolean debug) {
        return "";
    }
}
