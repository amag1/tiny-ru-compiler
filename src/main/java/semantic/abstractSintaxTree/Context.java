package semantic.abstractSintaxTree;

import semantic.symbolTable.*;

public class Context {
    private SymbolTableLookup st;
    private String callingClassName;
    private String callingMethodName;
    private String currentClassName;
    private boolean isSelf;


    public Context(SymbolTableLookup st) {
        this.st = st;
    }

    public Context(SymbolTableLookup st, String callingClassName) {
        this.st = st;
        this.callingClassName = callingClassName;
    }

    private Context(SymbolTableLookup st, String callingClassName,  String callingMethodName, String currentClassName, boolean isSelf) {
        this.st = st;
        this.callingClassName = callingClassName;
        this.callingMethodName = callingMethodName;
        this.currentClassName = currentClassName;
        this.isSelf = isSelf;
    }

    public VariableEntry getAttribute(String attributeName) {
        // El contexto es el método start
        if (this.currentClassName == null) {
            return getAttributeInStart(attributeName);
        }

        // El contexto es la clase actual
        if (this.isSelf) {
            return getAttributeInClass(attributeName, this.currentClassName);
        }

        // El contexto es la funcion desde la que fue llamada
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

    public Context clone(String className) {
        return new Context(this.st, this.callingClassName, this.callingMethodName, className, true);
    }

    public Context clone(String className, String callingMethodName) {
        return new Context(this.st, className, callingMethodName, className, false);
    }

    public  Context clone() {
        return new Context(this.st, this.callingClassName, this.callingMethodName, this.currentClassName, false);
    }

    public ClassEntry getClass(String className) {
        return st.getClassByName(className);
    }

    public ClassEntry getCallingClass() {
        return st.getClassByName(this.callingClassName);
    }

    public boolean isCallingClassScope() {
        return this.callingClassName.equals(this.currentClassName);
    }

    public MethodEntry getCallingMethod() {
        return  this.getMethod(this.callingMethodName);
    }
}
