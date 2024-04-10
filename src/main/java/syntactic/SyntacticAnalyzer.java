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
        bloqueMetodo();
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
        declVariableLocalesMetodo();
        sentenciaMetodo();
        match(Type.CLOSE_CURLY);
    }

    private void declVariableLocalesMetodo() throws SyntacticException, LexicalException {
        Type[] first = {Type.ID_CLASS, Type.ARRAY, Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        for (Type type : first) {
            if (getTokenType() == type) {
                declaracionVariablesLocales();
                declVariableLocalesMetodo();
            }
        }

        // Si no hay match, asumimos que deriva lambda
    }

    private void declaracionVariablesLocales() throws SyntacticException, LexicalException {
        tipo();
        listaDeclaracionVariables();
        match(Type.SEMICOLON);
    }

    private void listaDeclaracionVariables() throws SyntacticException, LexicalException {
        match(Type.ID);

        // Si el siguiente token no es una coma, asumimos que termino
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            listaDeclaracionVariables();
        }
    }

    private void sentenciaMetodo() throws SyntacticException, LexicalException {
        // Follow = }
        Type[] follow = {Type.CLOSE_CURLY};
        for (Type type : follow) {
            if (getTokenType() == type) {
                return;
            }
        }

        sentencia();
        sentenciaMetodo();
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

    private void sentencia() throws SyntacticException, LexicalException {
        // ;
        if (getTokenType() == Type.SEMICOLON) {
            match(Type.SEMICOLON);
            return;
        }

        // ret ⟨Expresion-O-Semicolon⟩
        if (getTokenType() == Type.KW_RET) {
            match(Type.KW_RET);
            expresionOSemicolon();
            return;
        }

        // if ( ⟨Expresión⟩ ) ⟨Sentencia⟩ ⟨Else-O-Lambda⟩
        if (getTokenType() == Type.KW_IF) {
            match(Type.KW_IF);
            match(Type.OPEN_PAR);
            expresion();
            match(Type.CLOSE_PAR);
            sentencia();
            elseOLambda();
            return;
        }

        // while ( ⟨Expresión⟩ ) ⟨Sentencia⟩
        if (getTokenType() == Type.KW_WHILE) {
            match(Type.KW_WHILE);
            match(Type.OPEN_PAR);
            expresion();
            match(Type.CLOSE_PAR);
            sentencia();
            return;
        }

        // ⟨Asignación⟩ ;
        Type[] first = {Type.ID, Type.KW_SELF};
        for (Type type : first) {
            if (getTokenType() == type) {
                asignacion();
                match(Type.SEMICOLON);
                return;
            }
        }

        // ( ⟨Expresión⟩ ) ;
        if (getTokenType() == Type.OPEN_PAR) {
            match(Type.OPEN_PAR);
            expresion();
            match(Type.CLOSE_PAR);
            match(Type.SEMICOLON);
            return;
        }

        // { ⟨Sentencia-Bloque⟩ }
        if (getTokenType() == Type.OPEN_CURLY) {
            match(Type.OPEN_CURLY);
            sentenciaBloque();
            match(Type.CLOSE_CURLY);
            return;
        }

        // Devolver error en caso de no matchear ninguno de los anteriores
        throwSyntacticException(Type.SEMICOLON, Type.KW_RET, Type.KW_IF, Type.KW_WHILE, Type.KW_SELF, Type.OPEN_PAR, Type.OPEN_CURLY);
    }
    private void expresionOSemicolon() throws SyntacticException, LexicalException  {
        if (getTokenType() == Type.SEMICOLON) {
            match(Type.SEMICOLON);
            return;
        }

        expresion();
        match(Type.SEMICOLON);
    }

    private void expresion() throws SyntacticException, LexicalException {
        try {
            // ⟨ExpAnd⟩ ⟨ExpOr`⟩
            expAnd();
            expOrPrima();
        } catch (SyntacticException e) {
            throwSyntacticException("expresión");
        }
    }

    private void elseOLambda() throws SyntacticException, LexicalException {
        // TODO
    }

    private void asignacion() throws SyntacticException, LexicalException {
        // TODO
    }

    private void sentenciaBloque() throws SyntacticException, LexicalException {
        // TODO
    }

    private void expAnd() throws SyntacticException, LexicalException {
        // ⟨ExpIgual⟩ ⟨ExpAnd`⟩
        expIgual();
        expAndPrima();
    }

    private void expOrPrima() throws SyntacticException, LexicalException {
        // TODO
    }

    private void expIgual() throws SyntacticException, LexicalException {
        // ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩
        expCompuesta();
        expIgualPrima();
    }

    private void expAndPrima() throws SyntacticException, LexicalException {
        // TODO
    }

    private void expCompuesta() throws SyntacticException, LexicalException {
        // ⟨ExpAd⟩ ⟨ExpAd`⟩
        expAd();
        expAdPrima();
    }

    private void expIgualPrima() throws SyntacticException, LexicalException {
        // TODO
    }

    private void expAd() throws SyntacticException, LexicalException {
        // ⟨ExpMul⟩ ⟨ExpAd`⟩
        expMul();
        expAdPrima();
    }

    private void expAdPrima() throws SyntacticException, LexicalException {
        // TODO
    }

    private void expMul() throws SyntacticException, LexicalException {
        // ⟨ExpUn⟩ ⟨ExpMul`⟩
        expUn();
        expMulPrima();
    }

    private void expUn() throws SyntacticException, LexicalException {
        // ( + | - | ! | ++ | -- ) ⟨ExpUn⟩
        Type[] first = {Type.PLUS, Type.MINUS, Type.NEG, Type.DPLUS, Type.DMINUS};
        for (Type type : first) {
            if (getTokenType() == type) {
                match(type);
                expUn();
                return;
            }
        }

        // ⟨Operando⟩
        operando();
    }

    private void expMulPrima() throws SyntacticException, LexicalException {
        // TODO
    }

    private void operando() throws SyntacticException, LexicalException {
        // nil | true | false | intLiteral | StrLiteral | charLiteral
        Type[] literals = {
                Type.KW_NIL,
                Type.KW_TRUE,
                Type.KW_FALSE,
                Type.INT_LITERAL,
                Type.STRING_LITERAL,
                Type.CHAR_LITERAL
        };
        for (Type type : literals) {
            if (getTokenType() == type) {
                match(type);
                return;
            }
        }

        // ⟨Primario⟩ ⟨Primarios⟩
        primario();
        primarios();
    }

    private void primario() throws SyntacticException, LexicalException {
        // ( ⟨Expresion⟩ ) ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.OPEN_PAR) {
            match(Type.OPEN_PAR);
            expresion();
            match(Type.CLOSE_PAR);
            encadenadoOLambda();
            return;
        }

        // self ⟨Encadenado⟩ | self
        if (getTokenType() == Type.KW_SELF) {
            match(Type.KW_SELF);

            // ⟨Encadenado⟩
            if (getTokenType() == Type.DOT) {
                encadenado();
            }
            return;
        }

        // ⟨AccesoVar⟩ | ⟨Llamada-Método⟩
        if (getTokenType() == Type.ID) {
            match(Type.ID);

            // ⟨Llamada-Método⟩
            if (getTokenType() == Type.OPEN_PAR) {
                llamadaMetodo();
                return;
            }

            // ⟨AccesoVar⟩
            // Puede ser lambda
            else {
                accesoVar();
                return;
            }
        }

        // idStruct . ⟨Llamada-Método⟩
        if (getTokenType() == Type.ID_CLASS) {
            match(Type.ID_CLASS);
            match(Type.DOT);
            llamadaMetodo();
            return;
        }

        // ⟨Llamada-Constructor⟩
        if (getTokenType() == Type.KW_NEW) {
            match(Type.KW_NEW);
            llamadaNew();
        }

        // Devolver error en otro caso
        Type[] expected = {
                Type.OPEN_CURLY,
                Type.KW_SELF,
                Type.ID,
                Type.ID_CLASS,
                Type.KW_NEW
        };
        throwSyntacticException(expected);
    }

    private void primarios() throws SyntacticException, LexicalException {
        // TODO
    }

    private void accesoVar() throws SyntacticException, LexicalException {
        // TODO
    }

    private void llamadaMetodo() throws SyntacticException, LexicalException {
        // TODO
    }

    private void llamadaNew() throws SyntacticException, LexicalException {
        // TODO
    }

    private void encadenadoOLambda() throws SyntacticException, LexicalException {
        // TODO
    }

    private void encadenado() throws SyntacticException, LexicalException {
        // TODO
    }



}
