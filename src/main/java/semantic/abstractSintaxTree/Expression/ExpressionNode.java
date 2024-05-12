package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

public abstract class ExpressionNode extends AbstractSyntaxNode {

    protected AttributeType attributeType;
    
    public abstract AttributeType getAttributeType(Context context) throws AstException;
}
