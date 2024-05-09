package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;
import semantic.JsonHelper;

import java.util.List;

public class BlockNode extends SentenceNode {
    private List<SentenceNode> sentences;

    public BlockNode(List<SentenceNode> sentences) {
        this.nodeType = "block";
        this.sentences = sentences;
    }

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("sentences", this.sentences, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
