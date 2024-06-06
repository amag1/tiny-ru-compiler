package semantic.abstractSintaxTree.Sentence;

import codeGeneration.MipsHelper;
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
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de retorno
 */
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

    /**
     * Valida que el tipo que se retorna sea compatible con el metodo actual
     */
    @Override
    public void validate(Context context) throws AstException {
        MethodEntry method = context.getCallingMethod();

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
                // Formatear los tipos en caso de que sean arrays
                String foundType = currentReturnType.isArray() ? "Array " + currentReturnType.getType() : currentReturnType.getType();
                String expectedType = method.getReturnType().isArray() ? "Array " + method.getReturnType().getType() : method.getReturnType().getType();
                throw new InvalidMethodReturn(expectedType, foundType, this.token);
            }
        }

        // Si el metodo actual no existe, estamos en el contructor
        else {
            // Chequea que no haya return en el constructor
            // Verifica que la clase no sea null (en ese caso estamos en start)
            if (method == null && context.getCallingClass() != null) {
                throw new ReturnInConstructorException(token);
            }
            // Metodo void, chequear que el return sea vacío
            if (returnValue != null) {
                throw new InvalidVoidMethodReturn(token);
            }

        }

        setReturn(true);
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);
        helper.comment("Generar codigo de retorno");

        if (returnValue != null) {
            helper.append(returnValue.generate(context, classEntry, methodEntry, debug));
        }

        helper.append("j " + helper.getEndLabel(methodEntry.getName(), classEntry.getName()));

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("returnValue", returnValue, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }


}
