package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.AttributeType;

/**
 * Nodo de sentencia if-else
 */
public class IfElseNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode thenBody;
    private SentenceNode elseBody;

    public IfElseNode(ExpressionNode condition, SentenceNode thenBody, SentenceNode elseBody) {
        this.nodeType = "ifSentence";
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;

        if (condition != null) {
            setToken(condition.getToken());
        }
        else {
            if (thenBody != null) {
                setToken(thenBody.getToken());
            }
            else {
                if (elseBody != null) {
                    setToken(elseBody.getToken());
                }
            }
        }
    }

    /**
     * Valida que la condicion sea booleana y analiza los bloques then y else
     * <p>
     * Si ambos bloques tienen return, entonces este tambien lo tiene
     */
    @Override
    public void validate(Context context) throws AstException {
        AttributeType conditionType = condition.getAttributeType(context);
        // La condicion debe ser booleana
        if (!conditionType.getType().equals("Bool")) {
            throw new ParameterTypeMismatchException("Bool", conditionType.toString(), condition.getToken());
        }

        int emptyBranches = 2;
        if (thenBody != null) {
            thenBody.validate(context);
            emptyBranches--;
        }

        if (elseBody != null) {
            elseBody.validate(context);
            emptyBranches--;
        }


        if (emptyBranches == 0) {
            setReturn(thenBody.hasReturn() && elseBody.hasReturn());
        }
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("condition", this.condition, indentationIndex) + "," +
                JsonHelper.json("thenBody", this.thenBody, indentationIndex) + "," +
                JsonHelper.json("elseBody", this.elseBody, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
