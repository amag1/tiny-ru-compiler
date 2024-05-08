package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;

import java.util.List;

public class BlockNode extends SentenceNode {
    private List<SentenceNode> sentences;

    public BlockNode(List<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return "";
    }
}
