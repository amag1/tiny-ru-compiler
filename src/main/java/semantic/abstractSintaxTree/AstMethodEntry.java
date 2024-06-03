package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.MissingReturnException;
import exceptions.semantic.syntaxTree.ReturnInConstructorException;
import lexical.Token;
import semantic.Json;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;

import java.util.ArrayList;

public class AstMethodEntry implements Json {

    String name;

    private final Token token;
    private boolean isStatic;
    public ArrayList<SentenceNode> sentences;

    public AstMethodEntry(Token token) {
        this.name = token.getLexem();
        this.token = token;
        this.sentences = new ArrayList<>();
    }

    public void validateSentences(Context context) throws AstException {
        if (name.equals(".")) {
            validateConstructor(context);
        }
        else {
            validateMethod(context);
        }
    }

    private void validateConstructor(Context context) throws AstException {
        boolean hasReturn = checkSentencesAndReturn(context);

        if (hasReturn) {
            throw new ReturnInConstructorException(token);
        }
    }

    private void validateMethod(Context context) throws AstException {
        boolean hasReturn = checkSentencesAndReturn(context);

        // Si el metodo no es void y no tiene return, entonces hay un error
        if (context.getCallingMethod() != null) {
            AttributeType returnType = context.getCallingMethod().getReturnType();
            if (returnType != null && !returnType.getType().equals("void") && !hasReturn) {
                throw new MissingReturnException(context.getCallingMethod().getName(), token);
            }
        }
    }

    private boolean checkSentencesAndReturn(Context context) throws AstException {
        boolean hasReturn = false;
        for (SentenceNode sentence : sentences) {
            sentence.validate(context);
            if (sentence.hasReturn()) {
                hasReturn = true;
            }
        }
        return hasReturn;
    }

    public String getName() {
        return name;
    }

    public void addSentence(SentenceNode sentence) {
        this.sentences.add(sentence);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public String toJson(int indentationIndex) {
        indentationIndex++;

        return "{" +
                JsonHelper.json("name", this.name, indentationIndex) + "," +
                JsonHelper.json("sentences", this.sentences, indentationIndex) +
                "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "}";
    }

    public String generate(Context context, ClassEntry classentry, MethodEntry methodEntry, boolean debug) {
        StringBuilder sb = new StringBuilder();
        for (SentenceNode sentence : sentences) {
            sb.append(sentence.generate(context, classentry, methodEntry, debug));
        }

        return sb.toString();
    }
}
