package semantic.abstractSintaxTree;

import semantic.Json;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public abstract class AbstractSyntaxNode implements Json {
    protected String nodeType;

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        return "";
    }
}
