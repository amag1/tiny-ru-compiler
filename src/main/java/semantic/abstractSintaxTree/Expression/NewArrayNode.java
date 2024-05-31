package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.NonIntArrayIndexException;
import exceptions.semantic.syntaxTree.NonIntArrayLengthException;
import lexical.Token;
import lexical.Type;
import location.Location;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

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

    public String generate(Context context, boolean debug) {
        // Obtener datos necesarios
        MipsHelper helper = new MipsHelper(debug);
        helper.comment("New Array");

        // Generar codigo para la expresion del tamaño
        helper.comment("Calculate array length");
        helper.append(lengthExpression.generate(context.reset(), debug));

        // Guardar en t0 el tamaño del array
        helper.sw("$a0", "($t0)");

        // Alocar memoria para el array
        helper.mutilply("$a0", "$a0", "4");
        helper.syscall(9);

        // Guardar direccion del array en t1
        helper.move("$t1", "$v0");
        helper.add("$a0", "$a0", "$t1"); // Store in the accumolator the final addres of the array

        // Iterar sobre el array y setear el default
        helper.comment("start loop");
        helper.append("start_set_default_array:");
        helper.branchOnEqual("$a0", "$t1", "end_set_default_array");

        helper.loadWord("$t3", "defaultValue"+ elementsType.getType());

        // Store in t3 the current address
        // Decrease by 4 for fix the offset difference
        helper.sw("$t3", "-4($a0)");
        helper.addIU("$a0", "$a0", -4);
        helper.jump("start_set_default_array");
        helper.append("end_set_default_array:");

        // Alocar array CIR
        helper.allocateMemory(2*32); // Dos words
        helper.sw("$t0", "0($v0)"); // Guardar tamaño en primer word
        helper.sw("$t1", "4($v0)"); // Guardar direccion en primer word

        // Guardar variable array
        helper.storeInAccumulator("($v0)");

        return helper.getString();
    }
}
