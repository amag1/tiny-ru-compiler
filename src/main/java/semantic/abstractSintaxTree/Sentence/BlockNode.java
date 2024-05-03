package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.SemanticException;

public class BlockNode extends SentenceNode{
    private  SentenceNode[] sentences;

    @Override
    public void validate() throws SemanticException {
        // TODO
    }

    public String toJson(int indentationIndex) {
        // TODO
        return  "";
    }
}
