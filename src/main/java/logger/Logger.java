package logger;

import exceptions.lexical.LexicalException;
import exceptions.syntactic.SyntacticException;
import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta para imprimir los resultados
 * Presenta métodos concretos para obtener los mensajes
 */
public abstract class Logger {
    /**
     * Las clases concretas que heredan de Logger deben definir una forma de mostrar el éxito léxico
     *
     * @param tokens resultado del análisis léxico
     */
    public abstract void LogLexicSuccess(List<Token> tokens);

    public abstract void LogSymbolTable(String symbolTableJson);

    protected List<String> GetLexicSuccessMessage(List<Token> tokens) {
        List result = new ArrayList<String>();
        // Header
        result.add("CORRECTO: ANALISIS LEXICO");
        result.add("| TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |");
        // Body
        for (Token token : tokens) {
            result.add("| " + token.getType() + " | " + token.getLexem() + " | " + token.getLine() + " (" + token.getColumn() + ") |");
        }

        return result;
    }

    protected List<String> GetLexicErrorMessage(LexicalException e) {
        List result = new ArrayList<String>();
        result.add("ERROR: LEXICO");
        result.add("| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |");
        result.add("| " + e.getLine() + " | " + e.getColumn() + " | " + e.getMessage() + " |");
        return result;
    }

    protected List<String> GetSyntacticSuccessMessage() {
        List result = new ArrayList<String>();
        result.add("CORRECTO: ANALISIS SINTACTICO");
        return result;
    }

    protected List<String> GetSyntacticErrorMessage(SyntacticException e) {
        List result = new ArrayList<String>();
        result.add("ERROR: SINTACTICO");
        result.add("| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |");
        result.add("| " + e.getLine() + " | " + e.getColumn() + " | " + e.getMessage() + " |");
        return result;
    }

    protected List<String> GetSemanticSymbolTableSuccessMessage() {
        List result = new ArrayList<String>();
        result.add("CORRECTO: ANALISIS SEMANTICO - TABLA DE SIMBOLOS");
        return result;
    }

    protected List<String> GetSemanticErrorMessage(SymbolTableException e) {
        List result = new ArrayList<String>();
        result.add("ERROR: SEMANTICO - DECLARACIONES");
        result.add("| NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |");
        result.add("| " + e.getLine() + " | " + e.getColumn() + " | " + e.getMessage() + " |");
        return result;
    }

}
