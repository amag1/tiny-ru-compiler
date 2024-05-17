package semantic.abstractSintaxTree;

import semantic.symbolTable.*;

public class Context {
    private SymbolTableLookup st;

    private String callingClassName;
    private String callingMethodName;
    private String currentClassName;
    private boolean isSelfAccess;

    public Context(SymbolTableLookup st) {
        this.st = st;
    }

    public Context(SymbolTableLookup st, String callingClassName) {
        this.st = st;
        this.callingClassName = callingClassName;
    }

    public Context(SymbolTableLookup st, String callingClassName, String callingMethodName, String currentClassName, boolean isSelfAccess) {
        this.st = st;
        this.callingClassName = callingClassName;
        this.callingMethodName = callingMethodName;
        this.currentClassName = currentClassName;
        this.isSelfAccess = isSelfAccess;
    }

    public VariableEntry getAttribute(String attributeName) {
        // Si el contexto esta restringido a self, buscar en la clase actual
        if (this.isSelfAccess) {
            return getAttributeInClass(attributeName, this.callingClassName);
        }

        // El contexto es una clase
        if (this.currentClassName != null) {
            return getAttributeInClass(attributeName, this.currentClassName);
        }

        // El contexto es el método start
        if (this.callingClassName == null) {
            return getAttributeInStart(attributeName);
        }

        // El contexto es una funcion
        return getAttributeInMethod(attributeName, this.callingMethodName, this.callingClassName);
    }

    private VariableEntry getAttributeInStart(String attName) {
        MethodEntry start = st.getStart();
        return start.getLocalVariable(attName);
    }

    private VariableEntry getAttributeInClass(String attName, String className) {
        ClassEntry classEntry = st.getClassByName(className);
        return classEntry.getAttribute(attName);
    }

    private VariableEntry getAttributeInMethod(String attributeName, String methodName, String className) {
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

    public MethodEntry getMethod(String methodName) {
        ClassEntry currentClass = st.getClassByName(currentClassName);
        if (currentClass == null) {
            return null;
        }

        return currentClass.getMethod(methodName);
    }

    public MethodEntry getConstructorByClass(String className) {
        ClassEntry currentClass = st.getClassByName(className);
        if (currentClass == null) {
            return null;
        }
        return currentClass.getConstructor();
    }

    public boolean checkTypes(AttributeType expectedType, AttributeType foundType) {
        if (foundType.getType().equals(expectedType.getType())) {
            return true;
        }

        // TODO chequar polimorfismo

        return false;
    }

    public Context clone(String callingClassName, String callingMethodName) {
        return new Context(this.st, callingClassName, callingMethodName, null, false);
    }

    public Context cloneSelfContext() {
        return new Context(this.st, this.callingClassName, this.currentClassName, null, true);
    }

    public Context cloneChainContext(String currentClassName) {
        return new Context(this.st, this.callingClassName, this.currentClassName, currentClassName, false);
    }

    public Context reset() {
        return new  Context(this.st, this.callingClassName, this.currentClassName, null, false);
    }

    public ClassEntry getClass(String className) {
        return st.getClassByName(className);
    }

    public ClassEntry getCallingClass() {
        return st.getClassByName(this.callingClassName);
    }

    public boolean isSelfContext() {
        return this.currentClassName == null || this.isSelfAccess;
    }

    public MethodEntry getCallingMethod() {
        if (this.callingMethodName == null) {
            return null;
        }

        return this.getMethod(this.callingMethodName);
    }
}
