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

import java.util.ArrayList;
import java.util.List;

public class ReturnNode extends SentenceNode {
    private ExpressionNode returnValue;

    public ReturnNode(ExpressionNode returnValue, Token token) {
        this.nodeType = "returnSentence";
        this.returnValue = returnValue;

        setToken(token);
    }

    public ReturnNode(Token token) {
        this.nodeType = "returnSentence";
        this.returnValue = null;

        setToken(token);
    }

    @Override
    public void validate(Context context) throws AstException {
        MethodEntry method = context.getCurrentMethod();

        // Devolver el tipo de la expresion
        List<AttributeType> returnTypes = new ArrayList<>();
        if (method.getReturnType() != null) {
            // Chequear que el tipo de retorno coincida
            AttributeType currentReturnType;
            if (returnValue == null) {
                currentReturnType = new AttributeType("void");
            }
            else {
                currentReturnType = returnValue.getAttributeType(context);
            }
            returnTypes.add(currentReturnType);
            if (!context.checkTypes(method.getReturnType(), currentReturnType)) {
                throw new InvalidMethodReturn(method.getReturnType().getType(), currentReturnType.getType(), getToken());
            }
        }
        else {
            // Metodo void, chequear que el return sea vacío
            if (returnValue != null) {
                throw new InvalidVoidMethodReturn(getToken());
            }
        }

        setReturn(true);
        addToReturnType(returnTypes);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("returnValue", returnValue, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
