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
        // ⟨Lista-Definiciones⟩ ⟨Start⟩
        listaDefiniciones();
        start();
        match(Type.EOF);
    }

    private void start() throws SyntacticException, LexicalException {
        // start ⟨Bloque-Método⟩
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

        // ⟨Impl⟩ ⟨Lista-Definiciones⟩
        if (getTokenType() == Type.KW_IMPL) {
            impl();
            listaDefiniciones();
            return;
        }

        // ⟨Struct⟩ ⟨Lista-Definiciones⟩
        if (getTokenType() == Type.KW_STRUCT) {
            struct();
            listaDefiniciones();
        }
    }

    private void struct() throws SyntacticException, LexicalException {
        // struct idStruct ⟨Struct-O-Herencia⟩
        match(Type.KW_STRUCT);
        match(Type.ID_CLASS);
        structOHerencia();
    }

    private void structOHerencia() throws SyntacticException, LexicalException {
        // ⟨Herencia⟩ { ⟨Struct-Atributo⟩ } | { ⟨Struct-Atributo⟩ }

        // No terminal Herencia es opcional. No hay un metodo encargado de matchearlo
        if (getTokenType() == Type.COLON) {
            herencia();
        }

        match(Type.OPEN_CURLY);
        structAtributo();
        match(Type.CLOSE_CURLY);
    }

    private void structAtributo() throws SyntacticException, LexicalException {
        // ⟨Atributo⟩ ⟨Struct-Atributo⟩ | λ

        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
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
        // ⟨Visibilidad⟩ ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ; | ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;

        // Visibilidad es opcional
        if (getTokenType() == Type.KW_PRI) {
            match(Type.KW_PRI);
        }

        tipo();
        listaDeclaracionVariables();
        match(Type.SEMICOLON);
    }

    private void impl() throws SyntacticException, LexicalException {
        // impl idStruct { ⟨Miembro⟩ ⟨Miembro-Opcional⟩ }
        match(Type.KW_IMPL);
        match(Type.ID_CLASS);
        match(Type.OPEN_CURLY);
        miembro();
        miembroOpcional();
        match(Type.CLOSE_CURLY);
    }

    private void miembroOpcional() throws SyntacticException, LexicalException {
        // ⟨Miembro⟩ ⟨Miembro-Opcional⟩ | λ

        // Siguientes de miembro opcional. Indica que el no terminal deriva lambda
        if (getTokenType() == Type.CLOSE_CURLY) {
            return;
        }

        miembro();
        miembroOpcional();
    }

    private void miembro() throws SyntacticException, LexicalException {
        // ⟨Método⟩ | ⟨Constructor⟩
        metodo();
        // TODO constructor
    }

    private void metodo() throws SyntacticException, LexicalException {
        // st fn idMetAt ⟨Argumentos-Formales⟩ -⟩ ⟨Tipo-Método⟩ ⟨Bloque-Método⟩
        // | fn idMetAt ⟨Argumentos-Formales⟩ -⟩ ⟨Tipo-Método⟩ ⟨Bloque-Método⟩

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
        // : ⟨Tipo⟩
        match(Type.COLON);
        tipo();
    }

    private void bloqueMetodo() throws SyntacticException, LexicalException {
        // { ⟨Decl-Var-Locales-Metodo⟩ ⟨Sentencia-Metodo⟩ }
        match(Type.OPEN_CURLY);
        declVariableLocalesMetodo();
        sentenciaMetodo();
        match(Type.CLOSE_CURLY);
    }

    private void declVariableLocalesMetodo() throws SyntacticException, LexicalException {
        // ⟨Decl-Var-Locales⟩ ⟨Decl-Var-Locales-Metodo⟩ | λ
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
        // ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;
        tipo();
        listaDeclaracionVariables();
        match(Type.SEMICOLON);
    }

    private void listaDeclaracionVariables() throws SyntacticException, LexicalException {
        // idMetAt ⟨Lambda-O-Variables⟩
        match(Type.ID);

        // Si el siguiente token no es una coma, asumimos que termino
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            listaDeclaracionVariables();
        }
    }

    private void sentenciaMetodo() throws SyntacticException, LexicalException {
        // ⟨Sentencia⟩ ⟨Sentencia-Metodo⟩ | λ

        // Follow = }
        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
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
        // ( ) | ( ⟨Lista-Argumentos-Formales⟩ )

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
        // ⟨Argumento-Formal⟩ ⟨Argumento-Formal-O-Lambda⟩
        argumentoFormal();
        argumentoFormalOLambda();
    }

    private void argumentoFormal() throws SyntacticException, LexicalException {
        // ⟨Tipo⟩ idMetAt
        tipo();
        match(Type.ID);
    }

    private void argumentoFormalOLambda() throws SyntacticException, LexicalException {
        // , ⟨Lista-Argumentos-Formales⟩ | λ
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            listaArgumentosFormales();
        }

        // Si el token no es una coma, asumimos que es lambda
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
        throwSyntacticException("tipo");
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

            // else ⟨Sentencia⟩ | λ
            if (getTokenType() == Type.KW_ELSE) {
                match(Type.KW_ELSE);
                sentencia();
            }

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

        // ⟨Sentencia-Simple⟩
        if (getTokenType() == Type.OPEN_PAR) {
            sentenciaSimple();
            return;
        }

        // ⟨Bloque⟩
        if (getTokenType() == Type.OPEN_CURLY) {
            bloque();
            return;
        }

        // Devolver error en caso de no matchear ninguno de los anteriores
        throwSyntacticException(Type.SEMICOLON, Type.KW_RET, Type.KW_IF, Type.KW_WHILE, Type.KW_SELF, Type.OPEN_PAR, Type.OPEN_CURLY);
    }

    private void sentenciaSimple() throws SyntacticException, LexicalException {
        // ( ⟨Expresión⟩ ) ;
        match(Type.OPEN_PAR);
        expresion();
        match(Type.CLOSE_PAR);
        match(Type.SEMICOLON);
    }

    private void bloque() throws SyntacticException, LexicalException {
        // { ⟨Sentencia-Bloque⟩ }
        match(Type.OPEN_CURLY);
        sentenciaBloque();
        match(Type.CLOSE_CURLY);
    }



    private void expresionOSemicolon() throws SyntacticException, LexicalException  {
        // ⟨Expresión⟩ ; | ;
        if (getTokenType() == Type.SEMICOLON) {
            match(Type.SEMICOLON);
            return;
        }

        expresion();
        match(Type.SEMICOLON);
    }

    private void expresion() throws SyntacticException, LexicalException {
        // ⟨Expresión⟩ ::= ⟨ExpOr⟩
        // ⟨ExpOr⟩ ::= ⟨ExpAnd⟩ ⟨ExpOr`⟩

        try {
            // ⟨ExpAnd⟩ ⟨ExpOr`⟩
            expAnd();
            expOrPrima();
        } catch (SyntacticException e) {
            throwSyntacticException("expresión");
        }
    }


    private void asignacion() throws SyntacticException, LexicalException {
        // ⟨AccesoVar-Simple⟩ = ⟨Expresión⟩
        if (getTokenType() == Type.ID) {
            accesoVarSimple();
            match(Type.EQUAL);
            expresion();
            return;
        }

        // self ⟨Encadenado-Simple⟩ = ⟨Expresión⟩
        if (getTokenType() == Type.KW_SELF) {
            match(Type.KW_SELF);
            encadenadoSimple();
            match(Type.EQUAL);
            expresion();
            return;
        }

        throwSyntacticException("asignación");
    }

    private void sentenciaBloque() throws SyntacticException, LexicalException {
        // ⟨Sentencia⟩ ⟨Sentencia-Bloque⟩ | λ
        Type[] first = {Type.SEMICOLON, Type.KW_RET, Type.KW_IF, Type.KW_WHILE, Type.KW_SELF, Type.OPEN_PAR, Type.OPEN_CURLY};
        for (Type type : first) {
            if (getTokenType() == type) {
                sentencia();
                sentenciaBloque();
            }
        }

        // Otro caso, lambda
    }

    private void expAnd() throws SyntacticException, LexicalException {
        // ⟨ExpIgual⟩ ⟨ExpAnd`⟩
        expIgual();
        expAndPrima();
    }

    private void expOrPrima() throws SyntacticException, LexicalException {
        // or ⟨ExpAnd⟩ ⟨ExpOr`⟩ | λ
        if (getTokenType() == Type.OR) {
            match(Type.OR);
            expAnd();
            expOrPrima();
        }
    }

    private void expIgual() throws SyntacticException, LexicalException {
        // ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩
        expCompuesta();
        expIgualPrima();
    }

    private void expAndPrima() throws SyntacticException, LexicalException {
        // && ⟨ExpIgual⟩ ⟨ExpAnd`⟩ | λ
        if (getTokenType() == Type.AND) {
            match(Type.AND);
            expIgual();
            expAdPrima();
        }

        // Otro caso, lambda
    }

    private void expCompuesta() throws SyntacticException, LexicalException {
        // ⟨ExpAd⟩ ⟨ExpCompuesta`⟩
        expAd();
        expAdPrima(); // TODO fix
    }

    private void expIgualPrima() throws SyntacticException, LexicalException {
        // ⟨OpIgual⟩ ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩ | λ
        Type[] opIgual = {Type.EQUAL, Type.NOT_EQUAL};
        for (Type type : opIgual) {
            if (getTokenType() == type) {
                expMul();
                expCompuesta();
                expIgualPrima();
                return;
            }
        }

        // Otro caso, lambda
    }

    private void expAd() throws SyntacticException, LexicalException {
        // ⟨ExpMul⟩ ⟨ExpAd`⟩
        expMul();
        expAdPrima();
    }

    private void expAdPrima() throws SyntacticException, LexicalException {
        // ⟨OpAd⟩ ⟨ExpMul⟩ ⟨ExpAd'⟩ | λ
        Type[] opAd = {Type.PLUS, Type.MINUS};
        for (Type type : opAd) {
            if (getTokenType() == type) {
                expMul();
                expAdPrima();
                return;
            }
        }
    }

    private void expMul() throws SyntacticException, LexicalException {
        // ⟨ExpUn⟩ ⟨ExpMul`⟩
        expUn();
        expMulPrima();
    }

    private void expUn() throws SyntacticException, LexicalException {
        // ⟨OpUnario⟩ ⟨ExpUn⟩ | ⟨Operando⟩
        Type[] opUnario = {Type.PLUS, Type.MINUS, Type.NEG, Type.DPLUS, Type.DMINUS};
        for (Type type : opUnario) {
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
        // ⟨OpMul⟩ ⟨ExpUn⟩ ⟨ExpMul`⟩ | λ
        Type[] opMul = {Type.MULT, Type.DIV, Type.MOD};
        for (Type type : opMul) {
            if (getTokenType() == type) {
                match(type);
                expUn();
                expMulPrima();
                return;
            }
        }

        // Otro caso, lambda
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
        // ⟨ExpresionParentizada⟩
        if (getTokenType() == Type.OPEN_PAR) {
            expresionParentizada();
            return;
        }

        // ⟨AccesoSelf⟩
        // ⟨AccesoSelf⟩ ::= self ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.KW_SELF) {
            match(Type.KW_SELF);

            // ⟨Encadenado⟩
            if (getTokenType() == Type.DOT) {
                encadenado();
            }
            return;
        }

        // id ⟨AccesoVar-O-Llamada-Método⟩
        if (getTokenType() == Type.ID) {
            match(Type.ID);
            accesoVarOLLamadaMetodo();
            return;
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
        throwSyntacticException();
    }

    private void expresionParentizada() throws SyntacticException, LexicalException {
        // ( ⟨Expresion⟩ ) ⟨Encadenado-O-Lambda⟩
        match(Type.OPEN_PAR);
        expresion();
        match(Type.CLOSE_PAR);

        if (getTokenType() == Type.DOT) {
            encadenado();
        }
    }

    private void accesoVarOLLamadaMetodo() throws SyntacticException, LexicalException {
        // ⟨Llamada-Método⟩
        if (getTokenType() == Type.OPEN_PAR) {
            llamadaMetodo();
        }

        // ⟨AccesoVar⟩
        // Puede ser lambda
        else {
            accesoVar();
        }
    }

    private void primarios() throws SyntacticException, LexicalException {
        // ⟨Encadenado⟩ | λ
        if (getTokenType() == Type.DOT) {
            encadenado();
        }

        // Otro caso, lambda
    }

    private void accesoVar() throws SyntacticException, LexicalException {
        // ⟨Encadenado⟩
        if (getTokenType() == Type.DOT) {
            encadenado();
            return;
        }

        // [ ⟨Expresión⟩ ]
        if (getTokenType() == Type.OPEN_BRACKET) {
            match(Type.OPEN_BRACKET);
            expresion();
            match(Type.CLOSE_BRACKET);

            // id [ ⟨Expresión⟩ ] ⟨Encadenado⟩
            if (getTokenType() == Type.DOT) {
                encadenado();
            }
        }

        Type[] expected = {Type.OPEN_BRACKET, Type.DOT};
        throwSyntacticException(expected);
    }

    private void llamadaMetodo() throws SyntacticException, LexicalException {
        //  ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        argumentosActuales();

        if (getTokenType() == Type.DOT) {
            encadenado();
        }
    }

    private void llamadaNew() throws SyntacticException, LexicalException {
        // idStruct ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.ID_CLASS) {
            match(Type.ID_CLASS);
            argumentosActuales();

            if (getTokenType() == Type.DOT) {
                encadenado();
            }

            return;
        }


        // ⟨Tipo-Primitivo⟩ [ ⟨Expresion⟩ ]
        Type[] primitive = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        for (Type type : primitive) {
            if (getTokenType() == type) {
                tipoPrimitivo();
                return;
            }
        }

        Type[] expected = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL, Type.ID_CLASS};
        throwSyntacticException(expected);
    }


    private void encadenado() throws SyntacticException, LexicalException {
        // . id ⟨Llamada-Método-Encadenado-O-AccesoVar⟩
        match(Type.DOT);
        match(Type.ID);

    }

    private void argumentosActuales() throws SyntacticException, LexicalException {
        // ( ⟨Lista-Expresiones⟩ ) | ( )
        match(Type.OPEN_PAR);

        if (getTokenType() == Type.CLOSE_PAR) {
            match(Type.CLOSE_PAR);
        }
        else {
            listaExpresiones();
            match(Type.CLOSE_PAR);
        }
    }

    private void accesoVarSimple() throws SyntacticException, LexicalException {
        // id ⟨Encadenado-O-Bracket⟩
        match(Type.ID);
        encadenadoOBracket();
    }

    private void encadenadoOBracket() throws SyntacticException, LexicalException {
        // ⟨Encadenado-Simple⟩ | [ ⟨Expresión⟩ ]
        // ⟨Encadenado-Simple⟩
        if (getTokenType() == Type.DOT) {
            encadenadoSimple();
            return;
        }

        // [ ⟨Expresión⟩ ]
        if (getTokenType() == Type.OPEN_BRACKET) {
            match(Type.OPEN_BRACKET);
            expresion();
            match(Type.CLOSE_BRACKET);
            return;
        }

        // Otro caso, devolver error
        throwSyntacticException(Type.DOT, Type.OPEN_BRACKET);
    }



    private void encadenadoSimple() throws SyntacticException, LexicalException {
        // . id ⟨Encadenado-Simple⟩ | λ
        if (getTokenType() == Type.ID) {
            match(Type.DOT);
            match(Type.ID);
            encadenadoSimple();
        }

        // Otro caso, lambda
    }


    private void listaExpresiones() throws SyntacticException, LexicalException {
        // ⟨Expresión⟩ ⟨Expresiones⟩
        expresion();

        // ⟨Expresiones⟩ ::= λ | , ⟨Lista-Expresiones⟩
        if (getTokenType() == Type.COMMA){
            match(Type.COMMA);
            listaExpresiones();
        }
    }
}
