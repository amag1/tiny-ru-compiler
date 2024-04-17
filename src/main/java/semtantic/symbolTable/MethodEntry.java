package semtantic.symbolTable;

import lexical.Token;

import java.util.Map;

public class MethodEntry {
    private String name;
    private Token token;
    private boolean isStatic;
    private boolean isInherited;
    private AttributeEntry returnType;
    private Map<Token, VariableEntry> formalParameters;
    private Map<Token, VariableEntry> localVariables;
    private int position;

}
