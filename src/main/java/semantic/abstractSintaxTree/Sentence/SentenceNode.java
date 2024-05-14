package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

import java.util.ArrayList;
import java.util.List;

public abstract class SentenceNode extends AbstractSyntaxNode {
    protected boolean hasReturn = false;
    // Se modelan los tipos de retorno como una lista porque expresiones como los if pueden retornar diferentes tipos
    protected List<AttributeType> returnTypes = new ArrayList<>();

    private Token token;

    public abstract void validate(Context context) throws AstException;

    public boolean hasReturn() {
        return hasReturn;
    }

    public List<AttributeType> getReturnType() {
        return returnTypes;
    }

    protected void addToReturnType(List<AttributeType> returnTypes) {
         this.returnTypes.addAll(returnTypes);
    }

    protected void setReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
