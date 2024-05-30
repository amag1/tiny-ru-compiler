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
        helper.sw("$t0", "$a0");

        // Alocar memoria para el array
        helper.mutilply("$a0", "$t0", "4");
        helper.syscall(9);

        // Guardar direccion del array en t1
        helper.sw("$t1", "$v0");

        // Iterar sobre el array y setear el default
        /**
         li $a0, 20  # Tamaño del array

         li $v0, 9  # syscall para alocar array
         syscall

         move $t1, $v0 # Guardar en t1 direccion7


         start_set_default_array:
         beqz $a0, end_set_default_array
         li $t3, 5
         sw $t3, ($t1)
         addiu $t1, $t1, 4
         addiu $a0, $a0, -4

         j start_set_default_array
         end_set_default_array:
         **/
        // TODO return cir

        // Guardar variable array





        return helper.getString();
    }
}
