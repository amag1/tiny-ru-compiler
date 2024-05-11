package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;

public class ParentizedExpressionNode extends PrimaryNode {

    private ExpressionNode expression;

    public ParentizedExpressionNode(ExpressionNode expression) {
        this.nodeType = "parentizedExpression";
        this.expression = expression;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", this.expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    @Override
    public AttributeType getAttributeType(SymbolTableLookup st) throws AstException {
        return expression.getAttributeType(st);
    }
}
