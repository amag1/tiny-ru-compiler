package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

public abstract class ExpressionNode extends AbstractSyntaxNode {

    protected AttributeType attributeType;

    protected Token token;

    public abstract AttributeType getAttributeType(Context context) throws AstException;

    public Token getToken() {
        return token;
    }

    protected void setToken(Token token) {
        this.token = token;
    }
}
