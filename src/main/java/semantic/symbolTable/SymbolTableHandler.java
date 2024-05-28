package semantic.symbolTable;

import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;

/**
 * Interfaz que define los metodos que deben ser implementados por la clase que maneja la tabla de simbolos
 * Permite inyectar dependencias al analizador sintactico
 */
public interface SymbolTableHandler {
    String toJson();

    void consolidate() throws SymbolTableException;

    void initNewClasses();

    void handleNewClass(Token token) throws SymbolTableException;

    void handleNewAttribute(Token att, AttributeType type, boolean isPrivate) throws SymbolTableException;

    void handleInheritance(AttributeType type) throws SymbolTableException;

    void handleNewImpl(Token token) throws SymbolTableException;

    void handleFinishImpl() throws SymbolTableException;

    void handleConstructor(Token token) throws SymbolTableException;

    void handleNewMethod(Token token, Boolean isStatic) throws SymbolTableException;

    void addMethodParam(Token paramToken, AttributeType type, int position) throws SymbolTableException;

    void setMethodReturn(AttributeType type);

    void handleLocalVar(Token variableToken, AttributeType type) throws SymbolTableException;

    void handleFinishMethod();

    void handleStart();

    SymbolTableLookup getSymbolTableLookup();

    SymbolTable getSymbolTable();

    ClassEntry getCurrentClass();

    MethodEntry getCurrentMethod();
}
