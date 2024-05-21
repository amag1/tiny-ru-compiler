package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import location.Location;
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

    private Context(SymbolTableLookup st, String callingClassName, String callingMethodName, String currentClassName, boolean isSelfAccess) {
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

        // El contexto es un encadenado
        if (this.currentClassName != null) {
            return getAttributeInClass(attributeName, this.currentClassName);
        }

        // El contexto es el método start
        if (this.callingClassName == null) {
            return getAttributeInStart(attributeName);
        }

        // El contexto es la clase
        if (this.callingMethodName == null) {
            return getAttributeInClass(attributeName, this.callingClassName);
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

        MethodEntry currentMethod;
        if (methodName.equals(".")) {
            currentMethod = currentClass.getConstructor();
        }
        else {
            currentMethod = currentClass.getMethod(methodName);
        }

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
        ClassEntry currentClass = null;

        if (currentClassName != null) {
            currentClass = st.getClassByName(currentClassName);
        } else {
            if (callingClassName != null) {
                currentClass = st.getClassByName(callingClassName);
            }
        }



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

        // Si es array puede ser nil
        if (expectedType.isArray()) {
            if (foundType.getType().equals(AttributeType.NilType.getType())) {
                return true;
            }
        }

        // Si el tipo no es un tipo primitivo, verificar si es una subclase
        if (expectedType.isPrimitive()) {
            return false;
        }

        return isSubclass(foundType, expectedType);
    }

    public Context clone(String callingClassName, String callingMethodName) {
        return new Context(this.st, callingClassName, callingMethodName, null, false);
    }

    public Context cloneSelfContext() {
        return new Context(this.st, this.callingClassName, this.callingMethodName, null, true);
    }

    public Context cloneChainContext(String currentClassName) {
        return new Context(this.st, this.callingClassName, this.callingMethodName, currentClassName, false);
    }

    public Context reset() {
        return new Context(this.st, this.callingClassName, this.callingMethodName, null, false);
    }

    public ClassEntry getClass(String className) {
        return st.getClassByName(className);
    }

    public ClassEntry getCallingClass() {
        if (this.callingClassName == null) {
            return  null;
        }
        return st.getClassByName(this.callingClassName);
    }

    public boolean isSelfContext() {
        return this.currentClassName == null || this.isSelfAccess;
    }

    public MethodEntry getCallingMethod() {
        if (this.callingMethodName == null) {
            return null;
        }

        if (this.callingMethodName.equals(".")) {
            ClassEntry currentClass = st.getClassByName(this.callingClassName);
            return  currentClass.getConstructor();
        }

        return this.getMethod(this.callingMethodName);
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

    public boolean isStart() {
        return this.callingClassName == null;
    }
}
