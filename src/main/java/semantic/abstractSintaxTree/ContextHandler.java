package semantic.abstractSintaxTree;

import lexical.Token;
import semantic.symbolTable.*;

public class ContextHandler {
    private SymbolTableLookup st;

    public VariableEntry getAttributeInStart(String attName) {
        MethodEntry start = st.getStart();
        return  start.getLocalVariable(attName);
    }

    public AttributeEntry getAttributeInClass(String attName, String className) {
        ClassEntry classEntry = st.getClassByName(className);
        return classEntry.getAttribute(attName);
    }

    public VariableEntry getAttributeInMethod(String attributeName, String methodName, String className) {
        ClassEntry currentClass = st.getClassByName(className);
        MethodEntry currentMethod = currentClass.getMethod(methodName);

        // Buscar en variables locales -> paremetros -> atributos de la clase
        VariableEntry attribute = currentMethod.getLocalVariable(attributeName);
        if (attribute == null) {
            attribute = currentMethod.getFormalParam(attributeName);
            if (attribute == null) {
                attribute = currentClass.getAttribute(attributeName);
            }
        }

        return attribute;
    }

    public MethodEntry getMethodInClass(String methodName, String className) {
        ClassEntry currentClass = st.getClassByName(className);
        return  currentClass.getMethod(methodName);
    }

    public MethodEntry getConstructorInClass(String className) {
        ClassEntry currentClass = st.getClassByName(className);
        return  currentClass.getConstructor();
    }
}
