package semantic.abstractSintaxTree.Sentence;

import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;

import java.util.List;

public class BlockNode extends SentenceNode {
    private List<SentenceNode> sentences;

    public BlockNode(List<SentenceNode> sentences) {
        this.nodeType = "block";
        this.sentences = sentences;

        if (!sentences.isEmpty()) {
            setToken(sentences.get(0).getToken());
        }
    }

    @Override
    public void validate(Context context) throws AstException {
        for (SentenceNode sentence : sentences) {
            sentence.validate(context);
            if (sentence.hasReturn()) {
                setReturn(true);
            }
        }
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("sentences", this.sentences, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
