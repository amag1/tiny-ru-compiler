package exceptions.semantic.symbolTable;

import lexical.Token;

import java.util.Set;

public class CyclicInheritanceException extends SymbolTableException {
    public CyclicInheritanceException(Token tok, Set<String> classes) {
        super("Herencia cíclica detectada entre las clases: " + String.join(", ", classes), tok.getLocation());
    }
}
