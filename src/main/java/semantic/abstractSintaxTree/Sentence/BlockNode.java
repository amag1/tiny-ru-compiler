package semantic.abstractSintaxTree.Sentence;

import codeGeneration.MipsHelper;
import exceptions.semantic.syntaxTree.AstException;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

import java.util.List;

/**
 * Nodo de bloque
 * <p>
 * Tiene una lista de sentencias
 */
public class BlockNode extends SentenceNode {
    private List<SentenceNode> sentences;

    public BlockNode(List<SentenceNode> sentences) {
        this.nodeType = "block";
        this.sentences = sentences;

        if (!sentences.isEmpty()) {
            setToken(sentences.get(0).getToken());
        }
    }

    /**
     * Revisa que cada sentencia del bloque sea valida
     */
    @Override
    public void validate(Context context) throws AstException {
        for (SentenceNode sentence : sentences) {
            sentence.validate(context);
            if (sentence.hasReturn()) {
                setReturn(true);
            }
        }
    }

    public String generate(Context context, ClassEntry classEntry, MethodEntry methodEntry, boolean debug) {
        MipsHelper helper = new MipsHelper(debug);
        helper.comment("Generar codigo de bloque");
        for (SentenceNode sentence : sentences) {
            helper.append(sentence.generate(context, classEntry, methodEntry, debug));
        }

        return helper.getString();
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("nodeType", this.nodeType, indentationIndex) + "," +
                JsonHelper.json("sentences", this.sentences, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }
}
