package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.NonIntArrayLengthException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de creación de un nuevo array
 * <p>
 * Representa la creación de un nuevo array de un tipo de elementos
 */
public class NewArrayNode extends PrimaryNode {
    private AttributeType elementsType;
    private ExpressionNode lengthExpression;

    public NewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        this.nodeType = "newArray";
        this.token = elementsTypeToken;
        this.lengthExpression = lengthExpression;
        this.elementsType = switch (elementsTypeToken.getType()) {
            case TYPE_INT -> AttributeType.IntType;
            case TYPE_STRING -> AttributeType.StrType;
            case TYPE_CHAR -> AttributeType.CharType;
            case TYPE_BOOL -> AttributeType.BoolType;
            default -> null; // Unreachable line: Only primitive types allowed
        };
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Validar que la expresion de la longitud se un entero
        AttributeType lengthExpressionType = this.lengthExpression.getAttributeType(context.reset());
        if (!context.checkTypes(AttributeType.IntType, lengthExpressionType)) {
            throw new NonIntArrayLengthException(this.token);
        }

        return new AttributeType(true, false, this.token);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("elementsType", this.elementsType, indentationIndex) + "," +
                JsonHelper.json("lengthExpression", this.lengthExpression, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        // Obtener datos necesarios
        MipsHelper helper = new MipsHelper(debug);
        helper.comment("New Array");

        // Generar codigo para la expresion del tamaño
        helper.comment("Calculate array length");
        helper.append(lengthExpression.generate(context, classEntry, methodEntry, debug));

        // Verificar que la expresion sea positiva
        helper.move("$t0", "$a0");
        helper.append("bltz $t0, invalid_array_index");
        // Pushear el tamaño del array
        helper.push("$a0");

        // Pushear valor default
        helper.loadWord("$t0", "defaultValue" + elementsType.getType());
        helper.push("$t0");


        helper.jumpAndLink("new_array");

        return helper.getString();
    }
}
