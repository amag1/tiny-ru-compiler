package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.symbolTable.AttributeType;

public abstract class ExpressionNode extends AbstractSyntaxNode {

    private AttributeType type;
    public abstract AttributeType getType() throws SemanticException;
}
