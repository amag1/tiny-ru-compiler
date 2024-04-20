package semtantic.symbolTable;

import exceptions.semantic.*;
import lexical.Token;
import lexical.Type;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableHandler {
    private SymbolTable st;

    public SymbolTableHandler() {
        this.st = new SymbolTable();
        initNewClasses();
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
}
