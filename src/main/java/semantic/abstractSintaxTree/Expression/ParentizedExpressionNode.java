package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de expresion parentizada
 */
public class ParentizedExpressionNode extends PrimaryNode {

    private ExpressionNode expression;

    public ParentizedExpressionNode(ExpressionNode expression) {
        this.nodeType = "parentizedExpression";
        this.expression = expression;
        this.token = expression.getToken();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", this.expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        return expression.getAttributeType(context);
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry method, boolean debug) {
        return expression.generate(context, classEntry, method, debug);
    }

    public String accessVariable(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        return expression.accessVariable(context, classEntry, methodEntry, debug);
    }
}
