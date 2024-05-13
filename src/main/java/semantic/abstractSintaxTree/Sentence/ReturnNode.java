package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.InvalidMethodReturn;
import exceptions.semantic.syntaxTree.InvalidVoidMethodReturn;
import lexical.Token;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.MethodEntry;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnValue;

    private Token token;

    public ReturnNode(ExpressionNode returnValue, Token token) {
        this.nodeType = "returnSentence";
        this.returnValue = returnValue;
        this.token = token;
    }

    public ReturnNode(Token token) {
        this.token = token;
        this.nodeType = "returnSentence";
        this.returnValue = null;
    }

    @Override
    public void validate(Context context) throws AstException {
        MethodEntry method = context.getCurrentMethod();
        if (method.getReturnType() != null) {
            // Chequear que el tipo de retorno coincida
            AttributeType currentReturnType = returnValue.getAttributeType(context);
            if (!context.checkTypes(method.getReturnType(), currentReturnType)) {
                throw new InvalidMethodReturn(method.getReturnType().getType(), currentReturnType.getType(), this.token);
            }
        }
        else {
            // Metodo void, chequear que el return sea vacío
            if (returnValue != null) {
                throw new InvalidVoidMethodReturn(token);
            }
        }

        setReturn(true);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("returnValue", returnValue, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
