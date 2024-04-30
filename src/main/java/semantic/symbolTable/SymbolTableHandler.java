package semantic.symbolTable;

import exceptions.semantic.SemanticException;
import lexical.Token;

/**
 * Interfaz que define los metodos que deben ser implementados por la clase que maneja la tabla de simbolos
 * Permite inyectar dependencias al analizador sintactico
 */
public interface SymbolTableHandler {
    String toJson();

    void consolidate() throws SemanticException;

    void initNewClasses();

    void handleNewClass(Token token) throws SemanticException;

    void handleNewAttribute(Token att, AttributeType type, boolean isPrivate) throws SemanticException;

    void handleInheritance(AttributeType type) throws SemanticException;

    void handleNewImpl(Token token) throws SemanticException;

    void handleFinishImpl() throws SemanticException;

    void handleConstructor(Token token) throws SemanticException;

    void handleNewMethod(Token token, Boolean isStatic) throws SemanticException;

    void addMethodParam(Token paramToken, AttributeType type, int position) throws SemanticException;

    void setMethodReturn(AttributeType type);

    void handleLocalVar(Token variableToken, AttributeType type) throws SemanticException;

    void handleFinishMethod();

    void handleStart();
}
