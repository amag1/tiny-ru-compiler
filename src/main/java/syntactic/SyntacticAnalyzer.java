package syntactic;

import exceptions.lexical.LexicalException;
import exceptions.syntactic.SyntacticException;
import lexical.Lexical;
import lexical.Type;

public class SyntacticAnalyzer extends AbstractSyntacticAnalyzer implements Syntactic {
    public SyntacticAnalyzer(Lexical lexicalAnalyzer) {
        super(lexicalAnalyzer);
    }

    /**
     * Llama por primera vez a next token y luego trata de analizar el programa
     *
     * @throws SyntacticException si ocurre un error sintactico
     * @throws LexicalException   si ocurre un error lexico
     */
    @Override
    public void analyze() throws SyntacticException, LexicalException {
        nextToken();
        program();
    }

    /**
     * A partir de acá, cada método está asociado a un no terminal de la gramática
     * En la carpeta /grammar está la gramática final en formato BNF
     */
    private void program() throws SyntacticException, LexicalException {
        listaDefiniciones();
        start();
        match(Type.EOF);
    }

    private void start() throws SyntacticException, LexicalException {
        match(Type.KW_START);
        match(Type.OPEN_CURLY);
        match(Type.CLOSE_CURLY);
    }

    private void listaDefiniciones() throws SyntacticException, LexicalException {
        Type[] follow = {Type.KW_START};
        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
        for (Type type : follow) {
            if (getTokenType() == type) {
                return;
            }
        }

        if (getTokenType() == Type.KW_IMPL) {
            impl();
            listaDefiniciones();
            return;
        }

        if (getTokenType() == Type.KW_STRUCT) {
            struct();
            listaDefiniciones();
        }
    }

    private void struct() throws SyntacticException, LexicalException {
        match(Type.KW_STRUCT);
        match(Type.ID_CLASS);
        structOHerencia();
    }

    private void structOHerencia() throws SyntacticException, LexicalException {
        // No terminal Herencia es opcional. No hay un metodo encargado de matchearlo
        if (getTokenType() == Type.COLON) {
            herencia();
        }

        match(Type.OPEN_CURLY);
        structAtributo();
        match(Type.CLOSE_CURLY);
    }

    private void structAtributo() throws SyntacticException, LexicalException {
        Type[] follow = {Type.CLOSE_CURLY};
        for (Type type : follow) {
            if (getTokenType() == type) {
                return;
            }
        }

        atributo();
        structAtributo();
    }

    private void atributo() throws SyntacticException, LexicalException {
        // Visibilidad es opcional
        if (getTokenType() == Type.KW_PRI) {
            match(Type.KW_PRI);
        }
        tipo();
        listaDeclaracionVariables();
        match(Type.SEMICOLON);
    }

    private void impl() throws SyntacticException, LexicalException {
        match(Type.KW_IMPL);
        match(Type.ID_CLASS);
        match(Type.OPEN_CURLY);
        miembro();
        miembroOpcional();
        match(Type.CLOSE_CURLY);

    }

    private void miembroOpcional() throws SyntacticException, LexicalException {
        // Siguientes de miembro opcional. Indica que el no terminal deriva lambda
        if (getTokenType() == Type.CLOSE_CURLY) {
            return;
        }

        miembro();
        miembroOpcional();
    }

    private void miembro() throws SyntacticException, LexicalException {
        metodo();
    }

    private void metodo() throws SyntacticException, LexicalException {
        // Opcional: forma-metodo
        if (getTokenType() == Type.KW_ST) {
            match(Type.KW_ST);
        }

        match(Type.KW_FN);
        match(Type.ID);
        argumentosFormales();
        match(Type.ARROW);
        tipoMetodo();
        bloqueMetodo();
    }

    private void herencia() throws SyntacticException, LexicalException {
        match(Type.COLON);
        tipo();
    }

    private void bloqueMetodo() throws SyntacticException, LexicalException {
        match(Type.OPEN_CURLY);
        match(Type.CLOSE_CURLY);
    }

    private void listaDeclaracionVariables() throws SyntacticException, LexicalException {
        match(Type.ID);

        // Si el siguiente token no es una coma, asumimos que termino
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            listaDeclaracionVariables();
        }
    }

    private void argumentosFormales() throws SyntacticException, LexicalException {
        match(Type.OPEN_PAR);

        // Si el siguiente token es un CLOSE_PAR, entonces no hay argumentos formales
        if (getTokenType() == Type.CLOSE_PAR) {
            match(Type.CLOSE_PAR);
            return;
        }

        // De otro modo, intentar matchear los atributos formales
        listaArgumentosFormales();
        match(Type.CLOSE_PAR);
    }

    private void listaArgumentosFormales() throws SyntacticException, LexicalException {
        argumentoFormal();
        argumentoFormalOLambda();
    }

    private void argumentoFormal() throws SyntacticException, LexicalException {
        tipo();
        match(Type.ID);
    }

    private void argumentoFormalOLambda() throws SyntacticException, LexicalException {
        // Si no viene una coma, asumimos que viene lambda
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            listaArgumentosFormales();
        }
    }


    private void tipoMetodo() throws SyntacticException, LexicalException {
        // El tipo de retorno de un método puede ser cualqueir tipo hallado en `tipo()`
        // O tambien puede ser void
        if (getTokenType() == Type.TYPE_VOID) {
            match(Type.TYPE_VOID);
            return;
        }

        tipo();
    }

    private void tipo() throws SyntacticException, LexicalException {
        // Un tipo puede ser un ID_CLASS
        if (getTokenType() == Type.ID_CLASS) {
            match(Type.ID_CLASS);
            return;
        }

        // Tambien puede ser Array seguido de un tipo primitivo
        // O simplemente un tipo primitivo
        if (getTokenType() == Type.ARRAY) {
            match(Type.ARRAY);
            tipoPrimitivo();
            return;
        }

        Type[] primitive = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        for (Type type : primitive) {
            if (getTokenType() == type) {
                tipoPrimitivo();
                return;
            }
        }

        // Lanzar error sintactico con TODOS los posibles tipos que podrían haber aparecido
        throwSyntacticException(Type.ID_CLASS, Type.ARRAY, Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL);
    }

    private void tipoPrimitivo() throws SyntacticException, LexicalException {
        Type[] first = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        match(first);
    }
}
