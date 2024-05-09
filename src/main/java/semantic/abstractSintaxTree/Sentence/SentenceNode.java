package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.symbolTable.SymbolTableException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;

public abstract class SentenceNode extends AbstractSyntaxNode {
    public abstract void validate() throws SymbolTableException;
}
