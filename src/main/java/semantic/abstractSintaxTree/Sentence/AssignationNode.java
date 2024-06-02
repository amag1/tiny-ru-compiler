package semantic.abstractSintaxTree.Sentence;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.abstractSintaxTree.Expression.PrimaryNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de asignacion
 * <p>
 * Tiene dos hijos, el lado izquierdo y el lado derecho de la asignacion
 */
public class AssignationNode extends SentenceNode {
    private PrimaryNode leftSide;
    private ExpressionNode rightSide;

    public AssignationNode(PrimaryNode leftSide, ExpressionNode rightSide) {
        this.nodeType = "assignation";
        this.leftSide = leftSide;
        this.rightSide = rightSide;

        setToken(leftSide.getToken());
    }

    /**
     * Valida que ambos lados de la asignacion sean compatibles
     */
    @Override
    public void validate(Context context) throws AstException {
        // Validar ambos lados y chequear que sean compatibles
        AttributeType leftType = leftSide.getAttributeType(context);
        AttributeType rightType = rightSide.getAttributeType(context);

        if (!context.checkTypes(leftType, rightType)) {
            throw new ParameterTypeMismatchException(leftType.toString(), rightType.toString(), rightSide.getToken());
        }
    }

    public String generate(ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        helper.pushSelfAddress(classEntry, methodEntry);

        // Acceder a la direccion del lado izquierdo
        helper.comment("Acceder a direccion del lado izquierdo");
        helper.append(leftSide.accessVariable(classEntry, methodEntry, debug));

        // Pushear la direccion obtenida
        helper.comment("Pushear la direccion obtenida");
        helper.push("$a0");

        // Generar el lado derecho de la asignacion
        helper.comment("Generar el lado derecho de la asignacion");
        helper.append(rightSide.generate(classEntry, methodEntry, debug));

        // Popear la direccion del lado izquierdo
        helper.comment("Popear la direccion del lado izquierdo");
        helper.pop("$t0");

        // Guardar el valor en la direccion obtenida
        helper.comment("Guardar el valor en la direccion obtenida");
        helper.sw("$a0", "0($t0)");

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("leftSide", this.leftSide, indentationIndex) + "," +
                JsonHelper.json("rightSide", this.rightSide, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
