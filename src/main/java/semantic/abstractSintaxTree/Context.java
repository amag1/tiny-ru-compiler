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
            // Evita un error cuando se compara un tipo primitivo con un array de ese tipo
            return (foundType.isArray() == expectedType.isArray());
        }

        // Si el tipo no es un tipo primitivo, verificar si es una subclase
        if (expectedType.isPrimitive()) {
            return false;
        }

        // De otro modo, estamos comparando dos clases
        return isSubclass(foundType, expectedType);
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
        if (this.currentMethodName == null) {
            return null;
        }

        return this.getMethod(this.currentMethodName);
    }

    private boolean isSubclass(AttributeType foundType, AttributeType expectedType) {
        // Nil siempre es un subtipo valido
        if (foundType.getType().equals(AttributeType.NilType.getType())) {
            return true;
        }

        ClassEntry foundClass = st.getClassByName(foundType.getType());
        ClassEntry expectedClass = st.getClassByName(expectedType.getType());

        // Buscar la clase actual en la jerarquia de herencia
        if (foundClass == null || expectedClass == null) {
            return false;
        }

        while (foundClass != null) {
            if (foundClass.getName().equals(expectedClass.getName())) {
                return true;
            }
            if (foundClass.getName().equals("Object")) {
                return false;
            }

            foundClass = st.getClassByName(foundClass.getInherits());
        }

        return false;
    }
}
