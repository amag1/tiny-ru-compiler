package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.NonIntArrayIndexException;
import exceptions.semantic.syntaxTree.UndeclaredVariableAccessException;
import exceptions.semantic.syntaxTree.VariableIsNotArrayException;
import lexical.Token;
import semantic.JsonHelper;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;
import semantic.symbolTable.VariableEntry;

public class ArrayAccessNode extends PrimaryNode {
    private String arrayName;
    private Token idToken;
    private ExpressionNode index;

    public ArrayAccessNode(Token arrayName, ExpressionNode index) {
        this.nodeType = "arrayAccess";
        this.arrayName = arrayName.getLexem();
        this.idToken = arrayName;
        this.index = index;
    }

    @Override
    public AttributeType getAttributeType(SymbolTableLookup st) throws AstException {
        // Check que el array exista
        VariableEntry arr = st.getAttribute(this.arrayName);
        if (arr == null) {
            throw new UndeclaredVariableAccessException(this.idToken);
        }

        // Check que el array sea un array
        if (!arr.getType().isArray()) {
            throw new VariableIsNotArrayException(this.idToken);
        }

        // Check que el indice sea de tipo entero
        if (!index.getAttributeType(st).getType().equals("Int")) {
            throw new NonIntArrayIndexException(this.idToken);
        }

        // TODO: hacer esto un poco mas bonito
        return new AttributeType(arr.getType().getType());
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
