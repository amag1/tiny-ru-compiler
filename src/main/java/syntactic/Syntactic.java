package syntactic;

import codeGeneration.CodeGenerator;
import exceptions.lexical.LexicalException;
import exceptions.semantic.symbolTable.SymbolTableException;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.syntactic.SyntacticException;

/**
 * Interfaz para analizadores sintácticos.
 */
public interface Syntactic {
    CodeGenerator analyze() throws LexicalException, SyntacticException, SymbolTableException, AstException;

    String getSymbolTableJson();

    String getAbstractSybolTreeJson();
}
