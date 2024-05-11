package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import semantic.Json;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.SymbolTableLookup;

import java.util.ArrayList;

public class AstMethodEntry implements Json {

    String name;
    public ArrayList<SentenceNode> sentences;

    public AstMethodEntry(String name) {
        this.name = name;
        this.sentences = new ArrayList<>();
    }

    public void validateSentences(SymbolTableLookup st) throws AstException {
        for (SentenceNode sentence : sentences) {
            sentence.validate(st);
        }
    }

    public String getName() {
        return name;
    }

    public void addSentence(SentenceNode sentence) {
        this.sentences.add(sentence);
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("name", this.name, indentationIndex) + "," +
                JsonHelper.json("sentences", this.sentences, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
