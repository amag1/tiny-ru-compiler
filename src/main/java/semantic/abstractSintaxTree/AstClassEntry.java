package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.symbolTable.MethodEntry;

import java.util.Map;

public class AstClassEntry {
    private AstMethodEntry[] methods;

    public AstMethodEntry[] getMethods() {
        return methods;
    }

    public  void validateSentences() throws SemanticException {
        for (AstMethodEntry currentMethod: methods) {
            currentMethod.validateSentences();
        }
    }
}
