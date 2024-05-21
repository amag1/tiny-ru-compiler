package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;

/**
 * Nodo de sentencia
 */
public abstract class SentenceNode extends AbstractSyntaxNode {
    protected boolean hasReturn = false;

    protected Token token;

    public Token getToken() {
        return token;
    }

    protected void setToken(Token token) {
        this.token = token;
    }

    /**
     * Valida la sentencia
     */
    public abstract void validate(Context context) throws AstException;

    /**
     * Indica si la sentencia tiene un return
     */
    public boolean hasReturn() {
        return hasReturn;
    }

    protected void setReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
}
