package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ReturnTypeMismatchException;
import exceptions.semantic.syntaxTree.UnexpectedReturnException;
import lexical.Token;
import semantic.Json;
import semantic.JsonHelper;
import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;

import java.util.ArrayList;
import java.util.List;

public class AstMethodEntry implements Json {

    String name;
    public ArrayList<SentenceNode> sentences;

    public AstMethodEntry(String name) {
        this.name = name;
        this.sentences = new ArrayList<>();
    }

    public void validateSentences(Context context) throws AstException {
        AttributeType expectedReturnType = context.getMethod(name).getReturnType();
        // Guardar una lista con todos los posibles returnTypes
        List<AttributeType> returnTypes = new ArrayList<>();
        for (SentenceNode sentence : sentences) {
            sentence.validate(context);

            // Si hay returnTypes, chequear que sean de tipos compatibles
            if (sentence.hasReturn()) {
                for (AttributeType returnType : sentence.getReturnType()) {
                    if (!context.checkTypes(expectedReturnType, returnType)) {
                        throw new ReturnTypeMismatchException(expectedReturnType.getType(), returnType.getType(), sentence.getToken());
                    }

                    returnTypes.add(returnType);
                }
            }
        }

        // Si no hay return statement, chequear que el método sea void
        if (this.name.equals(".")) {
            checkConstructorReturnType(returnTypes, context, sentences.get(0).getToken());
        }
        else {
            checkMethodReturnType(returnTypes, expectedReturnType);
        }
    }

    private void checkConstructorReturnType(List<AttributeType> returnTypes,Context context, Token token) throws UnexpectedReturnException {
      if (!returnTypes.isEmpty()) {
          String methodName = "constructor de " + context.getCallingClass().getName();
            throw new UnexpectedReturnException(methodName, token);
      }
    }

    private void checkMethodReturnType(List<AttributeType> returnTypes, AttributeType expectedReturnType) throws ReturnTypeMismatchException {
        if (returnTypes.isEmpty() && expectedReturnType != null && !expectedReturnType.getType().equals("void")) {
            // Obtener location
            Token token = new Token();
            if (!sentences.isEmpty()) {
                token = sentences.get(0).getToken();
            }
            throw new ReturnTypeMismatchException(expectedReturnType.getType(), "void", token);
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
