package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.symbolTable.MethodEntry;

import java.util.Map;

public class AstClassEntry {
    private Map<String, AstMethodEntry> methods;

    public Map<String, AstMethodEntry> getMethods() {
        return methods;
    }

    public  void validateSentences() throws SemanticException {
        for (Map.Entry<String,AstMethodEntry> entry: methods.entrySet()) {
            AstMethodEntry currentMethod = entry.getValue();
            currentMethod.validateSentences();
        }
    }
}
