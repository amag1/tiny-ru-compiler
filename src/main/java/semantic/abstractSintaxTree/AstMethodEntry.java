package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import semantic.abstractSintaxTree.Sentence.SentenceNode;

public class AstMethodEntry {
    public SentenceNode[] sentences;


    public void validateSentences() throws SemanticException {
        for (SentenceNode sentence: sentences) {
            sentence.validate();
        }
    }

}
