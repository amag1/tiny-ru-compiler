package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.OnlyVarException;
import exceptions.semantic.syntaxTree.SelfAccessInStaticMethod;
import exceptions.semantic.syntaxTree.UnaryTypeMismatchException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

public class UnaryOperationNode extends ExpressionNode {

    private ExpressionNode operating;
    private Operator operator;

    public UnaryOperationNode(ExpressionNode operating, Token operator) {
        this.nodeType = "unaryOperation";
        this.operating = operating;
        this.operator = new Operator(operator);
        this.token = operator;
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Chequear que el contexto no sea onlyVar
        if (context.isOnlyVar()) {
            throw new OnlyVarException(this.token);
        }

        String unaryOperator = operator.getToken().getLexem();
        Context operatingContext;
        if (unaryOperator.equals("++") || unaryOperator.equals("--")) {
            operatingContext = context.clone();
            operatingContext.setOnlyVar(true);
        }
        else {
            operatingContext = context.reset();
        }

        AttributeType operatingType = operating.getAttributeType(operatingContext);

        AttributeType operatorType = this.operator.getAttributeType();
        if (operatorType.getType().equals(operatingType.getType())) {
            return operator.getAttributeType();
        }
        else {
            throw new UnaryTypeMismatchException(operator.getToken(), operatorType.toString(), operatingType.toString());
        }

    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);
        // Comienza generando codigo para la subexpresion
        helper.comment("Generar codigo para operando de expresion unaria");
        helper.append(operating.accessVariable(context, classEntry, methodEntry, debug));

        // Generar codigo para la operacion
        helper.comment("Generar codigo para la operacion unaria");
        helper.append(operator.generate("$a0", debug));

        return helper.getString();
    }


    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("operating", this.operating, indentationIndex) + "," +
                JsonHelper.json("operator", this.operator, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
