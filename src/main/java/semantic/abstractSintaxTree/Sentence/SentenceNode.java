package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;

public abstract class SentenceNode extends AbstractSyntaxNode {
    protected boolean hasReturn = false;

    public abstract void validate(Context context) throws AstException;

    public boolean hasReturn() {
        return hasReturn;
    }

    protected void setReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
}
