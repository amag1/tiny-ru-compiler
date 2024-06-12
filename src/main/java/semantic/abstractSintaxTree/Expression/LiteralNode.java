package semantic.abstractSintaxTree.Expression;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.OnlyVarException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;


/**
 * Nodo literal
 * <p>
 * Tiene un tipo asociado que no necesita ser calculado
 */
public class LiteralNode extends OperatingNode {
    private String value;

    private static int counter;

    private int id;


    public LiteralNode(Token token) {
        this.id = counter++;
        this.token = token;
        this.attributeType = switch (token.getType()) {
            case INT_LITERAL -> AttributeType.IntType;
            case STRING_LITERAL -> AttributeType.StrType;
            case CHAR_LITERAL -> AttributeType.CharType;
            case KW_TRUE, KW_FALSE -> AttributeType.BoolType;
            default -> AttributeType.NilType;
        };

        this.nodeType = "literal";
        this.value = token.getLexem();
    }

    @Override
    public AttributeType getAttributeType(Context context) throws AstException {
        // Chequear que el contexto no sea onlyVar
        if (context.isOnlyVar()) {
            throw new OnlyVarException(this.token);
        }

        return this.attributeType;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("attributeType", this.attributeType.toString(), indentationIndex) + "," +
                JsonHelper.json("value", this.value, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);

        String type = "";
        String value = "";
        switch (attributeType.getType()) {
            case "Int":
                type = ".word";
                value = this.value;
                break;
            case "Str":
                type = ".asciiz";
                value = "\"" + this.value + "\"";
                break;
            case "Bool":
                type = ".word";
                value = this.value.equals("true") ? "1" : "0";
                break;
            case  "Char":
                type = ".word";
                value = "'" + this.value + "'";
                break;
            default:
                type = ".word";
                value = "0";
        }




        if (attributeType.getType().equals("Str")) {
            helper.startData();
            String literalName = "literal_" + id;
            helper.addDataLabel(literalName, type, value);
            helper.addDataLabel("str_" + literalName, ".word", literalName);
            helper.startText();
            helper.createStringCir("str_" + literalName);
            helper.loadAddress("$a0", "($t0)");
        } else {
            helper.load("$a0", value);
        }

        return helper.getString();
    }
}
