package semtantic.symbolTable;

import exceptions.semantic.InvalidInheritanceException;
import exceptions.semantic.RedefinedStructException;
import exceptions.semantic.SemanticException;
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

    public void handleInheritance(Token token) throws InvalidInheritanceException {
        if (!isValidInheritance(token)) {
            throw new InvalidInheritanceException(token);
        }
        ClassEntry currentClass = this.st.getCurrentClass();
        currentClass.setInherits(token.getLexem());
    }

    private boolean isValidInheritance(Token token) {
        Type[] invalidType = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL, Type.ARRAY};
        for (Type type : invalidType) {
            if (type == token.getType()) {
                return false;
            }
        }

        return true;
    }

}
