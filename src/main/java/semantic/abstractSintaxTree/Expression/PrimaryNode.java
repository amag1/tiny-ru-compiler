package semantic.abstractSintaxTree.Expression;

import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public abstract class PrimaryNode extends OperatingNode {
    public String accessVariable(ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        return "";
    }

}
