package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.*;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;
import semantic.symbolTable.VariableEntry;

public class ArrayAccessNode extends PrimaryNode {
    private String arrayName;
    /**
     * Expresion que representa el indice del array. Debe ser de tipo entero
     */
    private ExpressionNode index;

    private VariableEntry variable;

    public ArrayAccessNode(Token arrayName, ExpressionNode index) {
        this.nodeType = "arrayAccess";
        this.arrayName = arrayName.getLexem();
        this.index = index;
        this.token = arrayName;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Check que el array exista
        VariableEntry arr = context.getAttribute(this.arrayName);
        if (arr == null) {
            throw new UndeclaredVariableAccessException(this.getToken());
        }

        variable = arr;

        // Chequar si se puede acceder al atributo
        if (arr.isPrivate()) {
            // Si el atributo es heredado y privado, es inaccesible
            if (arr.isInherited()) {
                throw new UnaccesibleVariableException(this.token);
            }

            // Si el atributo es llamado desde otro scope, es inaccesible
            if (!context.isSelfContext()) {
                throw new UnaccesibleVariableException(this.token);
            }
        }


        // Check que el array sea un array
        if (!arr.getType().isArray()) {
            throw new VariableIsNotArrayException(this.getToken());
        }

        // Check que el indice sea de tipo entero
        AttributeType indexType = index.getAttributeType(context.reset());
        if (!indexType.equals(AttributeType.IntType)) {
            throw new NonIntArrayIndexException(this.getToken());
        }

        return new AttributeType(arr.getType().getArrayType());
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.comment("access to array");

        helper.append(getArrayAddress(context, classEntry, methodEntry, debug));

        helper.loadWord("$a0", "($t0)");

        return helper.getString();
    }

    public String accessVariable(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.comment("access to array");
        helper.append(getArrayAddress(context, classEntry, methodEntry, debug));

        helper.loadAddress("$a0", "($t0)");

        return helper.getString();
    }

    /**
     * Guarda en t0 la direccion del elemento del array a acceder
     *
     * @param debug
     * @return
     */
    private String getArrayAddress(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.checkNilPointer();

        // Acceder al cir del array
        helper.append(variable.loadWordByScope(debug, methodEntry));
        helper.loadWord("$t0", "8($a0)");
        helper.push("$t0");

        // Guardar tamaño del array
        helper.loadWord("$t0", "4($a0)");
        helper.push("$t0");

        // Obtener el indice
        helper.comment("Calculate index expression");
        helper.append(index.generate(context, classEntry, methodEntry, debug));

        // Chequear si el indice es menor a 0
        helper.append("slt $s0, $a0,  $zero");
        helper.append("bne $s0, $zero, exception_index_out_of_bounds");

        // Chequear si el indice es mayor al tamaño del array
        helper.pop("$t1");
        helper.append("slt  $s0, $t1, $a0");
        helper.append("bne $s0, $zero, exception_index_out_of_bounds");

        // Agregar el offset de indice al comienzo del array
        helper.pop("$t0");
        helper.mutilply("$a0", "$a0", "4");
        helper.add("$t0", "$t0", "$a0");

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("name", this.arrayName, indentationIndex) + "," +
                JsonHelper.json("index", this.index, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
