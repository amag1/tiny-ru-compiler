package semantic.symbolTable;

import exceptions.semantic.symbolTable.SymbolTableException;
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
    public void consolidate() throws SymbolTableException {

    }

    @Override
    public void initNewClasses() {

    }

    @Override
    public void handleNewClass(Token token) throws SymbolTableException {

    }

    @Override
    public void handleNewAttribute(Token att, AttributeType type, boolean isPrivate) throws SymbolTableException {

    }

    @Override
    public void handleInheritance(AttributeType type) throws SymbolTableException {

    }

    @Override
    public void handleNewImpl(Token token) throws SymbolTableException {

    }

    @Override
    public void handleFinishImpl() throws SymbolTableException {

    }

    @Override
    public void handleConstructor(Token token) throws SymbolTableException {

    }

    @Override
    public void handleNewMethod(Token token, Boolean isStatic) throws SymbolTableException {

    }

    @Override
    public void addMethodParam(Token paramToken, AttributeType type, int position) throws SymbolTableException {

    }

    @Override
    public void setMethodReturn(AttributeType type) {

    }

    @Override
    public void handleLocalVar(Token variableToken, AttributeType type) throws SymbolTableException {

    }

    @Override
    public void handleFinishMethod() {

    }

    @Override
    public void handleStart() {

    }

    @Override
    public SymbolTableLookup getSymbolTableLookup() {
        return null;
    }

    public ClassEntry getCurrentClass() {
        return new ClassEntry(new Token());
    }

    public MethodEntry getCurrentMethod() {
        return new MethodEntry();
    }
}
