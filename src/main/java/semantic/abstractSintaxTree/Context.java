package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import location.Location;
import semantic.symbolTable.*;

/**
 * Clase que representa el contexto de ejecución de un nodo del AST
 * Se utiliza para buscar variables y métodos en la tabla de símbolos
 */
public class Context {
    /**
     * Tabla de símbolos
     */
    private SymbolTableLookup st;

    private String callingClassName;
    private String callingMethodName;
    private String currentClassName;
    private boolean isSelfAccess;
    private boolean onlyVar;

    public Context(SymbolTableLookup st) {
        this.st = st;
    }

    public Context(SymbolTableLookup st, String callingClassName) {
        this.st = st;
        this.callingClassName = callingClassName;
    }

    private Context(SymbolTableLookup st, String callingClassName, String callingMethodName, String currentClassName, boolean isSelfAccess, boolean onlyVar) {
        this.st = st;
        this.callingClassName = callingClassName;
        this.callingMethodName = callingMethodName;
        this.currentClassName = currentClassName;
        this.isSelfAccess = isSelfAccess;
        this.onlyVar = onlyVar;
    }

    /**
     * Busca una variable en el contexto actual
     *
     * @param attributeName Nombre del atributo a buscar
     * @return VariableEntry con la información del atributo
     */
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

    /**
     * Busca una variable en el método start
     *
     * @param attName Nombre del atributo a buscar
     * @return VariableEntry con la información del atributo
     */
    private VariableEntry getAttributeInStart(String attName) {
        MethodEntry start = st.getStart();
        return start.getLocalVariable(attName);
    }

    /**
     * Busca una variable en una clase
     *
     * @param attName   Nombre del atributo a buscar
     * @param className Nombre de la clase en la que se buscará el atributo
     * @return VariableEntry con la información del atributo
     */
    private VariableEntry getAttributeInClass(String attName, String className) {
        ClassEntry classEntry = st.getClassByName(className);
        return classEntry.getAttribute(attName);
    }

    /**
     * Busca una variable en un método
     *
     * @param attributeName Nombre del atributo a buscar
     * @param methodName    Nombre del método en el que se buscará el atributo
     * @param className     Nombre de la clase en la que se buscará el método
     * @return VariableEntry con la información del atributo
     */
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
        }
        else {
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

    /**
     * Realiza el chequeo de tipos para dos tipos en particular
     *
     * @param expectedType Tipo esperado
     * @param foundType    Tipo encontrado
     * @return true si los tipos son compatibles, false en caso contrario
     */
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

    /**
     * @param callingClassName  Nombre de la clase que llama al método
     * @param callingMethodName Nombre del método que llama al método
     * @return Contexto clonado
     */
    public Context clone(String callingClassName, String callingMethodName) {
        return new Context(this.st, callingClassName, callingMethodName, null, false,false);
    }

    public Context clone() {
        return  new Context(
                this.st,
                this.callingClassName,
                this.callingMethodName,
                this.currentClassName,
                this.isSelfAccess,
                this.onlyVar
        );
    }

    /**
     * Setea el contexto actual
     */
    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    /**
     * Setea selfContext y currentClass
     * @param selfAccess
     */
    public void setSelfAccess(boolean selfAccess) {
        this.isSelfAccess = selfAccess;
    }


    /**
      * @return Un nuevo contexto conservando callingClasss y callingMethod del contexto actual
     */
    public Context reset() {
        return new Context(this.st, this.callingClassName, this.callingMethodName, null, false, false);
    }

    public void setOnlyVar(boolean onlyVar) {
        this.onlyVar = onlyVar;
    }

    public ClassEntry getClass(String className) {
        return st.getClassByName(className);
    }

    public ClassEntry getCallingClass() {
        if (this.callingClassName == null) {
            return null;
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
            return currentClass.getConstructor();
        }

        return this.getMethod(this.callingMethodName);
    }

    /**
     * Verifica si una clase es subclase de otra
     *
     * @param foundType    Tipo encontrado
     * @param expectedType Tipo esperado
     * @return true si foundType es subclase de expectedType, false en caso contrario
     */
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

    public boolean isOnlyVar() {
        return onlyVar;
    }
}
