package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;

public abstract class SentenceNode extends AbstractSyntaxNode {
    public abstract void validate() throws AstException;
}
