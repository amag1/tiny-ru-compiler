package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.symbolTable.SymbolTableLookup;

public abstract class SentenceNode extends AbstractSyntaxNode {
    public abstract void validate(SymbolTableLookup st) throws AstException;
}
