package semantic.abstractSintaxTree;

import semantic.Json;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public abstract class AbstractSyntaxNode implements Json {
    protected String nodeType;

    /**
     * Genera código MIPS específico para cada nodo
     * @param context
     * @param classEntry
     * @param methodEntry
     * @param debug
     * @return
     */
    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        return "";
    }
}
