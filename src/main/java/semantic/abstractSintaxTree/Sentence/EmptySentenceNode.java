package semantic.abstractSintaxTree.Sentence;

import semantic.JsonHelper;
import semantic.symbolTable.SymbolTableLookup;

public class EmptySentenceNode extends SentenceNode {

    public EmptySentenceNode() {
        this.nodeType = "emptySentence";
    }

    @Override
    public void validate(SymbolTableLookup st) {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;
        return "{" +
                JsonHelper.json("nodeType", nodeType, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
