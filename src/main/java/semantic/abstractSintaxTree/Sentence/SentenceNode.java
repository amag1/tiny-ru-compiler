package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;

public abstract class SentenceNode extends AbstractSyntaxNode {
    public abstract void validate(Context context) throws AstException;
}
