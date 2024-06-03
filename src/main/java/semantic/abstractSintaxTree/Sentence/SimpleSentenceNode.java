package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de sentencia simple
 */
public class SimpleSentenceNode extends SentenceNode {
    private ExpressionNode expression;

    public SimpleSentenceNode(ExpressionNode expression) {
        this.nodeType = "simpleSentence";
        this.expression = expression;

        if (expression != null) {
            setToken(expression.getToken());
        }
    }

    @Override
    public void validate(Context context) throws AstException {
        if (expression != null) {
            expression.getAttributeType(context);
        }
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("expression", expression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        if (expression != null) {
            return expression.generate(context, classEntry, methodEntry, debug);
        }
        return "";
    }
}
