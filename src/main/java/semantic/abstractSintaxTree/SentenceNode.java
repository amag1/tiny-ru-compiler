package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;

public abstract class SentenceNode extends  AbstractSyntaxNode {
    public abstract void validate() throws SemanticException;
}
