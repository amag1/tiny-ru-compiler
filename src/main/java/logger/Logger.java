package logger;

import exceptions.lexical.LexicalException;
import lexical.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class Logger {
    public abstract void LogLexicSuccess(List<Token> tokens);

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


}
