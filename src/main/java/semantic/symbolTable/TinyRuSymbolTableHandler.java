package semantic.symbolTable;

import exceptions.semantic.*;
import exceptions.semantic.ClassNotFoundException;
import lexical.Token;

import java.util.*;

/**
 * Implementacion del manejador de la tabla de simbolos de TinyRu
 * Contiene las acciones Semanticas que permiten llenar la tabla de simbolos
 * y consolidar la herencia
 */
public class TinyRuSymbolTableHandler implements SymbolTableHandler {
    private SymbolTable st;

    public TinyRuSymbolTableHandler() {
        this.st = new SymbolTable();
        initNewClasses();
    }

    public String toJson() {
        return this.st.toJson(0);
    }

    public void consolidate() throws SemanticException {
        // Chequear herencia valida
        consolidateInheritance();

        // Pasar atributos  y metodos heredados a las subclases
        setInheritance();

        // Chequea que las clases tengan Struct e Impl
        checkFoundImplAndStruct();

        // Chequea que las variables locales de start esten correctas
        checkTypesInMethod(st.getStart());
    }

    /**
     * Inicializa las clases predefinidas del lenguaje
     */
    public void initNewClasses() {
        // Generate classes
        List<ClassEntry> prefedinedClasses = new PredefinedClassCreator().generatePredefinedClasses();
        for (ClassEntry classEntry : prefedinedClasses) {
            this.st.insertClass(classEntry);
        }
    }

    /**
     * Crea una nueva clase en la tabla de simbolos y la setea como clase actual
     *
     * @param token El token con el nombre y la ubicacion de la clase
     * @throws SemanticException Si la clase con dicho nombre ya existia
     */
    public void handleNewClass(Token token) throws SemanticException {
        ClassEntry existingClass = this.st.getClassByName(token.getLexem());
        if (existingClass != null) {
            // Si la struct ya ha sido definida, lanzar un error
            if (existingClass.isFoundStruct()) {
                throw new RedefinedStructException(token);
            }
            existingClass.setFoundStruct(true);
            this.st.setCurrentClass(existingClass);
        }
        else {
            ClassEntry newClass = new ClassEntry(token);
            newClass.setFoundStruct(true);
            this.st.insertClass(newClass);
            this.st.setCurrentClass(newClass);
        }
    }

    /**
     * Anade un atributo a la clase actual
     *
     * @param att       El token con el nombre y la ubicacion del atributo
     * @param type      El tipo del atributo
     * @param isPrivate Si el atributo es privado
     * @throws RedefinedVariableException Si el atributo ya ha sido definido
     */
    public void handleNewAttribute(Token att, AttributeType type, boolean isPrivate) throws RedefinedVariableException {
        ClassEntry currentClass = this.st.getCurrentClass();
        if (currentClass.getAttribute(att.getLexem()) != null) {
            throw new RedefinedVariableException(att);
        }

        // Create new attribute
        AttributeEntry newAtt = new AttributeEntry(type, att, isPrivate);
        currentClass.addAttribute(newAtt);
    }

    /**
     * Setea la clase de la que hereda la clase actual
     *
     * @param type El tipo del que se desea heredar
     * @throws InvalidInheritanceException Si se hereda de una clase invalida (clases predefinidas)
     */
    public void handleInheritance(AttributeType type) throws InvalidInheritanceException {
        if (!isValidInheritance(type)) {
            throw new InvalidInheritanceException(type.getToken());
        }
        ClassEntry currentClass = this.st.getCurrentClass();
        currentClass.setInherits(type.getType());
    }

    private boolean isValidInheritance(AttributeType type) {
        return !type.isArray() && !type.isPrimitive();
    }

    /**
     * Revisa que cada clase haya encontrado un struct e implement
     *
     * @throws SemanticException Si no se encuentra algun struct o implement
     */
    private void checkFoundImplAndStruct() throws SemanticException {
        for (ClassEntry currentClass : this.st.getClasses()) {
            if (!currentClass.isFoundStruct()) {
                throw new UndefinedStructException(currentClass);
            }
            if (!currentClass.isFoundImpl()) {
                throw new UndefinedImplException(currentClass);
            }
        }
    }

    /**
     * Chequea si el struct del implement ya existe o si no lo crea.
     * En caso de existir, chequea también si ya se ha encontrado un implement para ese struct
     *
     * @param token
     * @throws SemanticException
     */
    public void handleNewImpl(Token token) throws SemanticException {
        ClassEntry currentClass = this.st.getClassByName(token.getLexem());
        if (currentClass != null) {
            // Si ya se ha encontrado un implement para el struct, lanzar un error
            if (currentClass.isFoundImpl()) {
                throw new RedefinedImplementException(currentClass, token.getLocation());
            }
        }

        else {
            // Si no existe el struct, se crea
            currentClass = new ClassEntry(token);
            this.st.insertClass(currentClass);
        }

        currentClass.setFoundImpl(true);
        this.st.setCurrentClass(currentClass);
    }

    /**
     * Chequea si el metodo constructor ha sido llamado y actualiza la tabla de símbolos
     *
     * @throws SemanticException Si no se encuentra un constructor para la clase
     */
    public void handleFinishImpl() throws SemanticException {

        // Chequea si se ha declarado el constructor
        ClassEntry currentClass = st.getCurrentClass();
        if (!currentClass.isHasConstructor()) {
            throw new MissingConstructorException(currentClass);
        }

        // Actualiza la tabla de símbolos
        st.setCurrentClass(null);
    }

    /**
     * Chequea que el struct no posea ya un constructor definido.
     * Agrega el constructor al struct
     *
     * @param token El token del constructor
     * @throws SemanticException Si ya existia un constructor para esa clase
     */
    public void handleConstructor(Token token) throws SemanticException {
        // Chequea si ya se ha declarado el constructor
        ClassEntry currentClass = st.getCurrentClass();
        if (currentClass.isHasConstructor()) {
            throw new RedefinedConstructorException(currentClass, token.getLocation());
        }

        // Agrega el constructor a la clase
        MethodEntry constructor = new MethodEntry(token, false);
        constructor.setReturnType(new AttributeType(false, false, currentClass.getToken()));
        st.setCurrentMethod(constructor);
        currentClass.setConstructor(constructor);
        currentClass.setHasConstructor(true);
    }

    /**
     * Consolida la herencia de las clases
     *
     * @throws ClassNotFoundException     Si la clase de la que hereda no existe
     * @throws CyclicInheritanceException Si se detecta herencia cíclica
     */
    private void consolidateInheritance() throws ClassNotFoundException, CyclicInheritanceException {
        // Recorre todas las clases
        // Para cada una, revisar que la clase de la que hereda existe
        // al mismo tiempo crear un nuevo set con las clases que ya se han visitado para detectar herencia cíclica
        Map<String, ClassEntry> existingClasses = new HashMap<>();
        for (ClassEntry classEntry : this.st.getClasses()) {
            String inherits = classEntry.getInherits();
            if (!inherits.equals("Object")) {
                ClassEntry parent = this.st.getClassByName(inherits);
                if (parent == null) {
                    throw new ClassNotFoundException(classEntry.getToken(), inherits);
                }
                existingClasses.put(classEntry.getName(), classEntry);
            }
        }

        // Chequear herencia ciclica
        while (!existingClasses.isEmpty()) {
            String firstNode = existingClasses.values().iterator().next().getName();
            // Eliminar primer item del hashmap
            existingClasses.remove(firstNode);
            Set<String> visited = new HashSet<>();
            Stack<String> path = new Stack<>();
            path.add(firstNode);

            while (!path.isEmpty()) {
                String current = path.pop();
                visited.add(current);

                ClassEntry currentClass = this.st.getClassByName(current);
                String inherits = currentClass.getInherits();
                // Ignorar clases que heredan de object
                if (!inherits.equals("Object")) {
                    // Si la clase actual ya ha sido visitada, entonces hay herencia cíclica
                    if (visited.contains(inherits)) {
                        throw new CyclicInheritanceException(currentClass.getToken(), visited);
                    }
                    // Si no, agregar la clase de la que hereda a la pila
                    else {
                        path.add(inherits);
                    }
                }
            }

        }
    }

    /**
     * Realiza las acciones necesarias para la consolidacion relacionadas con herencia
     *
     * @throws SemanticException
     */
    private void setInheritance() throws SemanticException {
        for (ClassEntry classEntry : this.st.getClasses()) {
            if (!classEntry.handledInheritance()) {
                // Chequear que todos los atributos tengan un tipo definido
                // Los errores pueden llegar desordenados ya que los atributos se guardan en un map
                checkAttributeTypes(classEntry);
                String inherits = classEntry.getInherits();
                if (!inherits.equals("Object")) {
                    setInheritanceWrapped(classEntry);
                }
                else {
                    classEntry.setHandledInheritance(true);
                }
            }
        }
    }

    /**
     * @param classEntry La clase a la que se le chequearán los tipos de los atributos
     * @throws TypeNotFoundException Si el tipo de algun atributo no existe
     */
    private void checkAttributeTypes(ClassEntry classEntry) throws TypeNotFoundException {
        // Chequear tipos para todos los atributos
        for (Map.Entry<String, AttributeEntry> entry : classEntry.getAttributes().entrySet()) {
            AttributeEntry attribute = entry.getValue();
            AttributeType type = attribute.getType();
            if (!checkTypeExists(type)) {
                throw new TypeNotFoundException(attribute.getToken(), type.getType());
            }
        }

        // Chequear tipos en variables locales de metodos
        for (Map.Entry<String, MethodEntry> entry : classEntry.getMethods().entrySet()) {
            MethodEntry method = entry.getValue();
            checkTypesInMethod(method);
        }

        // Chequear tipos en el constructor
        checkTypesInMethod(classEntry.getConstructor());
    }

    /**
     * @param method El metodo a chequear
     * @throws TypeNotFoundException Si el tipo en algun parametro formal o variable local no existe
     */
    private void checkTypesInMethod(MethodEntry method) throws TypeNotFoundException {
        if (method == null) {
            return;
        }

        for (Map.Entry<String, VariableEntry> localVarEntry : method.getLocalVariables().entrySet()) {
            VariableEntry localVar = localVarEntry.getValue();
            AttributeType type = localVar.getType();
            if (!checkTypeExists(type)) {
                throw new TypeNotFoundException(type.getToken(), localVar.getType().getType());
            }
        }

        for (Map.Entry<String, VariableEntry> methodInputEntry : method.getFormalParameters().entrySet()) {
            VariableEntry methodInput = methodInputEntry.getValue();
            AttributeType type = methodInput.getType();
            if (!checkTypeExists(type)) {
                throw new TypeNotFoundException(type.getToken(), methodInput.getType().getType());
            }
        }

        if (method.getReturnType() != null && !checkTypeExists(method.getReturnType())) {
            throw new TypeNotFoundException(method.getReturnType().getToken(), method.getReturnType().getType());
        }
    }

    /**
     * @param type El tipo a chequear
     * @return Un booleano que indica si el tipo existe o no
     */
    private boolean checkTypeExists(AttributeType type) {
        // Si el tipo es un struct, chequear que exista
        if (!type.isPrimitive() && !type.isArray()) {
            ClassEntry typeClass = this.st.getClassByName(type.getType());
            return typeClass != null;
        }

        return true;
    }

    /**
     * Metodo recursivo para setear la herencia de una clase
     * Si la clase hereda da alguna clase que no sea object, maneja la herencia de la superclase antes
     *
     * @param classEntry La clase cuya herencia quiere manejarse
     * @throws SemanticException
     */
    private void setInheritanceWrapped(ClassEntry classEntry) throws SemanticException {
        ClassEntry parent = this.st.getClassByName(classEntry.getInherits());
        if (!parent.getInherits().equals("Object") && !parent.handledInheritance()) {
            setInheritanceWrapped(parent);
        }

        setInheritedAttributes(classEntry, parent);
        setInheritedMethods(classEntry, parent);

        classEntry.setHandledInheritance(true);
    }

    /**
     * Agrega los atributos heredados de la clase padre a la clase actual
     * Tambien actualiza la posicion de todos los atributos
     *
     * @param classEntry La clase que quiere actualizarse
     * @param parent     La clase padre, de la cual se heredan los atributos
     * @throws SemanticException Si se redefine un atributo heredado
     */
    public void setInheritedAttributes(ClassEntry classEntry, ClassEntry parent) throws SemanticException {
        for (Map.Entry<String, AttributeEntry> entry : parent.getAttributes().entrySet()) {
            AttributeEntry attribute = entry.getValue();
            AttributeEntry newAttribute = new AttributeEntry(attribute.getType(), attribute.getToken(), attribute.isPrivate());
            newAttribute.setInherited(true);
            newAttribute.setPosition(attribute.getPosition());

            // Cheque que el atributo no sea redefinido en classEntry
            AttributeEntry redefinedAttribute = classEntry.getAttribute(attribute.getName());
            if (redefinedAttribute != null) {
                throw new RedefinedInheritedAttributeException(redefinedAttribute.getToken());
            }

            // Add attribute to class
            classEntry.addInheritedAttributeAtPosition(newAttribute, newAttribute.getPosition());
        }
    }

    /**
     * Agrega los metodos heredados de la clase padre a la clase actual
     *
     * @param currentClass La clase actual
     * @param parent       La clase padre
     * @throws OverridenMethodException Si se cambia la firma de un metodo heredado
     */
    public void setInheritedMethods(ClassEntry currentClass, ClassEntry parent) throws OverridenMethodException, StaticMethodOverridenException {
        int position = 0;
        for (Map.Entry<String, MethodEntry> entry : parent.getMethods().entrySet()) {
            MethodEntry parenMethod = entry.getValue();

            // Chequeamos si el metodo es redefinido
            MethodEntry currentClassMethod = currentClass.getMethod(parenMethod.getName());
            if (currentClassMethod != null) {
                // Chequea que el metodo heredado no sea estatico
                if (parenMethod.isStatic()) {
                    throw new StaticMethodOverridenException(currentClassMethod.getToken(), parenMethod.getName());
                }

                // Chequear que se mantenga la firma del metodo
                checkSignatures(currentClassMethod, parenMethod);

                currentClassMethod.setRedefined(true);
                currentClassMethod.setInherited(true);
                currentClassMethod.setPosition(parenMethod.getPosition());
            }
            else {
                // Metodo heredado
                currentClassMethod = parenMethod.copy();
                currentClassMethod.setInherited(true);
                currentClassMethod.setPosition(parenMethod.getPosition());
                currentClass.addInheritedMethod(currentClassMethod);
            }

            position++;
        }

        // Setea la posicion para los metodos no heredados
        for (Map.Entry<String, MethodEntry> entry : currentClass.getMethods().entrySet()) {
            MethodEntry method = entry.getValue();
            if (!method.isInherited() && !method.isRedefined()) {
                method.setPosition(position++);
            }
        }

    }

    public void checkSignatures(MethodEntry existingMethod, MethodEntry inheritedMethod) throws OverridenMethodException {
        // Chequear el tipo de retorno
        AttributeType existingReturnType = existingMethod.getReturnType();
        AttributeType inheritedReturnType = inheritedMethod.getReturnType();

        if (existingReturnType == null) {
            if (inheritedReturnType != null) {
                throw new OverridenMethodException(existingMethod.getToken(), inheritedMethod.getName());
            }
        }
        else {
            if (!existingReturnType.equals(inheritedReturnType)) {
                throw new OverridenMethodException(existingMethod.getToken(), inheritedMethod.getName());
            }
        }

        // Chequar que si ambos o ninguno es estático
        if (existingMethod.isStatic() != inheritedMethod.isStatic()) {
            throw new OverridenMethodException(existingMethod.getToken(), inheritedMethod.getName());
        }

        // Chequear que los parametros sean iguales
        if (!formalParametersMatch(existingMethod, inheritedMethod)) {
            throw new OverridenMethodException(existingMethod.getToken(), inheritedMethod.getName());
        }
    }

    /**
     * Dados dos metodos, revisa que sus paremetros formales coincidan
     *
     * @param existingMethod  El metodo actual
     * @param inheritedMethod El metodo heredado con el que se quiere comparar
     * @return Un booleano representando si los parametros formales coinciden
     */
    private boolean formalParametersMatch(MethodEntry existingMethod, MethodEntry inheritedMethod) {
        Map<String, VariableEntry> existingParams = existingMethod.getFormalParameters();
        Map<String, VariableEntry> inheritedParams = inheritedMethod.getFormalParameters();

        if (existingParams.size() != inheritedParams.size()) {
            return false;
        }

        for (Map.Entry<String, VariableEntry> entry : existingParams.entrySet()) {
            String name = entry.getKey();
            VariableEntry existingParam = entry.getValue();

            // get the parameter in the same position in the inherited method
            for (Map.Entry<String, VariableEntry> inheritedEntry : inheritedParams.entrySet()) {
                VariableEntry inheritedParam = inheritedEntry.getValue();
                if (inheritedParam.getPosition() == existingParam.getPosition()) {
                    if (!existingParam.getType().getType().equals(inheritedParam.getType().getType())) {
                        return false;
                    }
                }
            }
        }


        return true;
    }

    /**
     * Agrega un nuevo metodo a la clase actual.
     * Si hay otro metodo definido con el mismo nombre en la clase lanza error
     *
     * @param token
     * @param isStatic
     * @throws SemanticException
     */
    public void handleNewMethod(Token token, Boolean isStatic) throws SemanticException {
        ClassEntry currentClass = st.getCurrentClass();

        // Chequea si ya existe el metodo
        MethodEntry existingMethod = currentClass.getMethod(token.getLexem());
        if (existingMethod != null) {
            throw new RedefinedMethodException(existingMethod, token.getLocation());
        }

        // Agrega el nuevo metodo
        MethodEntry newMethod = new MethodEntry(token, isStatic);
        currentClass.addMethod(newMethod);

        // Actualiza tabla de simbolos
        st.setCurrentMethod(newMethod);
    }

    /**
     * Agrega un nuevo parametro formal al metodo actual.
     * Si hay otro parametro definido con el mismo nombre en el metodo lanza error
     *
     * @param paramToken
     * @param type
     * @throws SemanticException
     */
    public void addMethodParam(Token paramToken, AttributeType type, int position) throws SemanticException {
        MethodEntry currentMethod = st.getCurrentMethod();

        // Cheque si ya se ha definido un parametro con ese nombre
        VariableEntry existingParam = currentMethod.getFormalParam(paramToken.getLexem());
        if (existingParam != null) {
            throw new RedefinedVariableException(paramToken);
        }

        // Agrega el parametro formal a la lista
        VariableEntry formalParam = new VariableEntry(type, paramToken, position);
        currentMethod.addFormalParam(formalParam);
    }

    /**
     * Setea el tipo de retorno al metodo actual
     *
     * @param type
     */
    public void setMethodReturn(AttributeType type) {
        st.getCurrentMethod().setReturnType(type);
    }

    /**
     * Agrega una variable local al metodo
     * Si ya existe la variable entre los parametros o variables locales del metodo, lanza error
     *
     * @param variableToken
     * @param type
     * @throws SemanticException
     */
    public void handleLocalVar(Token variableToken, AttributeType type) throws SemanticException {
        MethodEntry currentMethod = st.getCurrentMethod();

        // Chequea si la variable está definida como parametro formal del método
        VariableEntry existingVar = currentMethod.getFormalParam(variableToken.getLexem());
        if (existingVar != null) {
            throw new RedefinedVariableException(variableToken);
        }

        // Chequa si la variable está definida dentro del cuerpo del método
        existingVar = currentMethod.getLocalVariable(variableToken.getLexem());
        if (existingVar != null) {
            throw new RedefinedVariableException(variableToken);
        }

        // Agrega la variable al metodo
        VariableEntry localVar = new VariableEntry(type, variableToken);
        currentMethod.addLocalVariable(localVar);
    }

    /**
     * Setea el metodo actual como null
     */
    public void handleFinishMethod() {
        st.setCurrentMethod(null);
    }

    public void handleStart() {
        st.setCurrentMethod(st.getStart());
    }
}
