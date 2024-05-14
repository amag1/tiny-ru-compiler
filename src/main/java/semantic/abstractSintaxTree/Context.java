package semantic.abstractSintaxTree;

import semantic.symbolTable.*;

public class Context {
    private SymbolTableLookup st;

    private String callingClassName;
    private String currentClassName;
    private String currentMethodName;

    private boolean isSelf;

    public Context(SymbolTableLookup st) {
        this.st = st;
    }

    public Context(SymbolTableLookup st, String callingClassName) {
        this.st = st;
        this.callingClassName = callingClassName;
    }

    private Context(SymbolTableLookup st, String callingClassName, String currentClassName, String currentMethodName) {
        this.st = st;
        this.callingClassName = callingClassName;
        this.currentClassName = currentClassName;
        this.currentMethodName = currentMethodName;
    }

    public VariableEntry getAttribute(String attributeName) {
        // Si el contexto esta restringido a self, buscar en la clase actual
        if (this.isSelf) {
            return getAttributeInClass(attributeName, this.currentClassName);
        }

        // El contexto es el método start
        if (this.currentClassName == null) {
            return getAttributeInStart(attributeName);
        }

        // El contexto es una clase
        if (this.currentMethodName == null) {
            return getAttributeInClass(attributeName, this.currentClassName);
        }

        // El contexto es una funcion
        return getAttributeInMethod(attributeName, this.currentMethodName, this.currentClassName);
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
        if (methodName == "start") {
            return st.getStart();
        }

        ClassEntry currentClass = st.getClassByName(currentClassName);
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

    public Context clone(String currentClassName, String currentMethodName) {
        return new Context(this.st, this.callingClassName, currentClassName, currentMethodName);
    }

    public Context cloneSelfContext() {
        Context newContext = new Context(this.st, this.callingClassName, this.currentClassName, this.currentMethodName);
        newContext.isSelf = true;
        return newContext;
    }

    public ClassEntry getClass(String className) {
        return st.getClassByName(className);
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public ClassEntry getCallingClass() {
        return st.getClassByName(this.callingClassName);
    }

    public boolean isCallingClassScope() {
        return this.callingClassName.equals(this.currentClassName);
    }

    public MethodEntry getCurrentMethod() {
        return  this.getMethod(this.currentMethodName);
    }
}
