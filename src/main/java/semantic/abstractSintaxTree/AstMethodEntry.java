package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Sentence.SentenceNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AstMethodEntry {

    String name;
    public ArrayList<SentenceNode> sentences;

    public AstMethodEntry(String name) {
        this.name = name;
        this.sentences = new ArrayList<>();
    }

    public void validateSentences() throws SemanticException {
        for (SentenceNode sentence: sentences) {
            sentence.validate();
        }
    }

    public String getName() {
        return name;
    }

    public void addSentence(SentenceNode sentence) {
        this.sentences.add(sentence);
    }

    public String toJson() {
        String json = "";
        for (SentenceNode sentenceNode : this.sentences) {
            json += sentenceNode.toJson(1);
        }
        return json;
    }
}
