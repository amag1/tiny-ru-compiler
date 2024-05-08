package semantic.abstractSintaxTree.Sentence;

public class EmptySentenceNode extends SentenceNode {

    public EmptySentenceNode() {
        this.nodeType = "emptySentence";
    }

    @Override
    public void validate() {
        // TODO
    }

    public String toJson(int indentationIndex) {
        return "";
    }
}
