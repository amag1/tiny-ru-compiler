package semantic.symbolTable;

import exceptions.semantic.SemanticException;
import lexical.Token;

/**
 * Clase que implementa la interfaz SymbolTableHandler
 * Los metodos no presentan ninguna funcionalidad
 * Su utilidad es permitir la inyeccion de dependencias al analizador sintactico
 * De esta manera, podemos probar el analizador sintactico sin necesidad de implementar la tabla de simbolos
 */
public class DummySymbolTableHandler implements SymbolTableHandler {
    @Override
    public String toJson() {
        return "";
    }

    @Override
    public void consolidate() throws SemanticException {

    }

    @Override
    public void initNewClasses() {

    }

    @Override
    public void handleNewClass(Token token) throws SemanticException {

    }

    @Override
    public void handleNewAttribute(Token att, AttributeType type, boolean isPrivate) throws SemanticException {

    }

    @Override
    public void handleInheritance(AttributeType type) throws SemanticException {

    }

    @Override
    public void handleNewImpl(Token token) throws SemanticException {

    }

    @Override
    public void handleFinishImpl() throws SemanticException {

    }

    @Override
    public void handleConstructor(Token token) throws SemanticException {

    }

    @Override
    public void handleNewMethod(Token token, Boolean isStatic) throws SemanticException {

    }

    @Override
    public void addMethodParam(Token paramToken, AttributeType type, int position) throws SemanticException {

    }

    @Override
    public void setMethodReturn(AttributeType type) {

    }

    @Override
    public void handleLocalVar(Token variableToken, AttributeType type) throws SemanticException {

    }

    @Override
    public void handleFinishMethod() {

    }

    @Override
    public void handleStart() {

    }

    public ClassEntry getCurrentClass() {return new ClassEntry(new Token());}

    public MethodEntry getCurrentMethod() {return new MethodEntry();}
}
