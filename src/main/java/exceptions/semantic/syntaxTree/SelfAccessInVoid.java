package exceptions.semantic.syntaxTree;

import lexical.Token;

public class SelfAccessInVoid extends AstException {
    public SelfAccessInVoid(Token token) {
        super("No se puede acceder a self en start", token.getLocation());
    }
}
