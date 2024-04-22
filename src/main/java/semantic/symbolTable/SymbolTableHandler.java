package semantic.symbolTable;

import exceptions.semantic.*;
import exceptions.semantic.ClassNotFoundException;
import lexical.Token;

import java.util.*;

public class SymbolTableHandler {
    private SymbolTable st;

    public SymbolTableHandler() {
        this.st = new SymbolTable();
        initNewClasses();
    }

    public String toJson() {
        return this.st.toJson();
    }

    public void consolidate() throws SemanticException {
        // Chequear herencia valida
        consolidateInheritance();
        // Pasar atributos heredados a las subclases
        // TODO: este metodo tambien deberia manejar los metodos heredados
        setInheritedAttributes();
    }

    public void initNewClasses() {
        // Generate classes
        List<ClassEntry> prefedinedClasses = new ArrayList<ClassEntry>();
        for (ClassEntry classEntry : prefedinedClasses) {
            this.st.insertClass(classEntry);
        }
    }

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

    public void handleNewAttribute(Token att, AttributeType type, boolean isPrivate) throws RedefinedVariableException {
        ClassEntry currentClass = this.st.getCurrentClass();
        if (currentClass.getAttribute(att.getLexem()) != null) {
            throw new RedefinedVariableException(att);
        }

        // Create new attribute
        AttributeEntry newAtt = new AttributeEntry(type, att, isPrivate);
        currentClass.addAttribute(newAtt);
    }

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
     * @throws SemanticException
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
     * @param token
     * @throws SemanticException
     */
    public void handleConstructor(Token token) throws SemanticException {
        // Chequea si ya se ha declarado el constructor
        ClassEntry currentClass = st.getCurrentClass();
        if (currentClass.isHasConstructor()) {
            throw new RedefinedConstructorException(currentClass, token.getLocation());
        }

        // Agrega el constructor a la clase
        MethodEntry constructor = new MethodEntry();
        currentClass.setConstructor(constructor);
        currentClass.setHasConstructor(true);
    }

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

    private void setInheritedAttributes() {
        for (ClassEntry classEntry : this.st.getClasses()) {
            if (!classEntry.handledInheritance()) {
                String inherits = classEntry.getInherits();
                if (!inherits.equals("Object")) {
                    setInheritedAttributesWrapped(classEntry);
                }
                else {
                    classEntry.setHandledInheritance(true);
                }
            }
        }
    }

    private void setInheritedAttributesWrapped(ClassEntry classEntry) {
        ClassEntry parent = this.st.getClassByName(classEntry.getInherits());
        if (!parent.getInherits().equals("Object")) {
            setInheritedAttributesWrapped(parent);
        }

        int position = 0;
        for (Map.Entry<String, AttributeEntry> entry : parent.getAttributes().entrySet()) {
            if (!entry.getValue().isPrivate()) {
                // Copy attribute
                AttributeEntry attribute = entry.getValue();
                AttributeEntry newAttribute = new AttributeEntry(attribute.getType(), attribute.getToken(), attribute.isPrivate());
                newAttribute.setInherited(true);
                newAttribute.setPosition(position);
                // Add attribute to class
                classEntry.addAttribute(newAttribute);
            }

            position++;
        }

        // Set position for non inherited attributes
        for (Map.Entry<String, AttributeEntry> entry : classEntry.getAttributes().entrySet()) {
            AttributeEntry attribute = entry.getValue();
            if (!attribute.isInherited()) {
                attribute.setPosition(position++);
            }
        }

        classEntry.setHandledInheritance(true);
    }

    public void handleNewMethod(Token token, Boolean isStatic) throws SemanticException {
        ClassEntry currentClass = st.getCurrentClass();

        // Chequea si ya existe el metodo
        MethodEntry existingMethod = currentClass.getMethod(token.getLexem());
        if (existingMethod != null) {
            throw new RedefinedMethodException(existingMethod, token.getLocation()) ;
        }

        // Agrega el nuevo metodo
        MethodEntry newMethod = new MethodEntry(token, isStatic);
        currentClass.addMethod(newMethod);

        // Actualiza tabla de simbolos
        st.setCurrentMethod(newMethod);
    }

    public void addMethodParam() throws SemanticException {
        // TODO
    }

    public void setMethodReturn(AttributeType type) throws SemanticException {
        st.getCurrentMethod().setReturnType(type);
    }
}
