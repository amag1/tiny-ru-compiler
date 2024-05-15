package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;

public abstract class SentenceNode extends AbstractSyntaxNode {
    protected boolean hasReturn = false;

    protected Token token;

    public Token getToken() {
        return token;
    }

    protected void setToken(Token token) {
        this.token = token;
    }

    public abstract void validate(Context context) throws AstException;

    public boolean hasReturn() {
        return hasReturn;
    }

    protected void setReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
}
