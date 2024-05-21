package exceptions.semantic.syntaxTree;

import lexical.Token;

public class SelfAccessInStaticMethod extends AstException {
    public SelfAccessInStaticMethod(Token token) {
        super("No se puede acceder a self en un método estático", token.getLocation());
    }
}
