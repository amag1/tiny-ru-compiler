package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.InvalidMethodReturn;
import exceptions.semantic.syntaxTree.InvalidVoidMethodReturn;
import exceptions.semantic.syntaxTree.ReturnInConstructorException;
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

        AttributeType currentReturnType;
        if (returnValue != null) {
            currentReturnType = returnValue.getAttributeType(context);
        }
        else {
            // Si retornamos una sentencia vacia, entonces el tipo de retorno es void
            currentReturnType = new AttributeType("void");
        }

        // Si el metodo existe y tiene retorno, verificar que el tipo de retorno sea valido
        if (method != null && method.getReturnType() != null) {
            // Chequear que el tipo de retorno coincida
            if (!context.checkTypes(method.getReturnType(), currentReturnType)) {
                throw new InvalidMethodReturn(method.getReturnType().getType(), currentReturnType.getType(), this.token);
            }
        }

        // Si el metodo actual no existe, estamos en el contructor
        else {
            if (method == null) {
                throw new ReturnInConstructorException(token);
            }
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
