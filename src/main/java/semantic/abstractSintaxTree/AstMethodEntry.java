package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Sentence.SentenceNode;

import java.util.ArrayList;
import java.util.HashMap;

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
}
