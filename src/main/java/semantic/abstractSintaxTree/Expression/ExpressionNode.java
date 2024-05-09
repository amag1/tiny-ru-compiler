package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;

public abstract class ExpressionNode extends AbstractSyntaxNode {

    protected AttributeType attributeType;
    
    public abstract AttributeType getAttributeType(SymbolTableLookup st) throws AstException;
}
