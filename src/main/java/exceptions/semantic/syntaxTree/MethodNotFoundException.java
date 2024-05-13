package exceptions.semantic.syntaxTree;

import lexical.Token;

public class MethodNotFoundException extends AstException {
    public MethodNotFoundException(Token methodName) {
        super("Metodo no encontrado: " + methodName.getLexem(), methodName.getLocation());
    }
}
