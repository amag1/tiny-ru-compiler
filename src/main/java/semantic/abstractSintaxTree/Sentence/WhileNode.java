package semantic.abstractSintaxTree.Sentence;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.abstractSintaxTree.Expression.ExpressionNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

/**
 * Nodo de sentencia while
 */
public class WhileNode extends SentenceNode {
    private ExpressionNode condition;
    private SentenceNode loopBody;
    private static int counter;
    private int id;

    public WhileNode(ExpressionNode condition, SentenceNode loopBody) {
        this.nodeType = "whileSentence";
        this.condition = condition;
        this.loopBody = loopBody;

        setToken(condition.getToken());
        id = counter;
        counter++;
    }

    /**
     * Valida que la condicion sea booleana y analiza el bloque del while
     * <p>
     * No tiene return aunque se halle alguno en el cuerpo del bucle
     */
    @Override
    public void validate(Context context) throws AstException {
        AttributeType conditionType = condition.getAttributeType(context);
        // La condicion debe ser booleana
        if (!conditionType.getType().equals("Bool")) {
            throw new ParameterTypeMismatchException("Bool", conditionType.toString(), condition.getToken());
        }

        loopBody.validate(context);
    }


    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);
        helper.comment("Generar codigo de bucle while");
        helper.append("while_" + id + ":");

        helper.comment("Generar codigo para condicion de bucle while");
        helper.append(condition.generate(context, classEntry, methodEntry, debug));

        helper.comment("Si la condicion es falsa, saltar al final del bucle");
        helper.append("beqz $a0, end_while_" + id);

        helper.comment("Generar codigo para cuerpo del bucle while");
        helper.append(loopBody.generate(context, classEntry, methodEntry, debug));

        helper.comment("Volver al inicio del bucle");
        helper.append("j while_" + id);

        helper.append("end_while_" + id + ":");

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("condition", condition, indentationIndex) + "," +
                JsonHelper.json("loopBody", loopBody, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
