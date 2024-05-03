package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.symbolTable.AttributeType;

public abstract class ExpressionNode extends AbstractSyntaxNode {

    protected AttributeType attributeType;
    public abstract AttributeType getAttributeType() throws SemanticException;
}
