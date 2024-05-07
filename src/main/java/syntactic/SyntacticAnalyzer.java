package syntactic;

import exceptions.lexical.LexicalException;
import exceptions.semantic.InvalidInheritanceException;
import exceptions.semantic.SemanticException;
import exceptions.syntactic.SyntacticException;
import lexical.Lexical;
import lexical.Token;
import lexical.Type;
import semantic.abstractSintaxTree.AstHandler;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.TinyRuAstHandler;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableHandler;
import semantic.symbolTable.TinyRuSymbolTableHandler;

import java.util.*;

/**
 * Analizador sintáctico concreto.
 * Implementa la interfaz Syntactic para analizar un programa en el lenguaje TinyRU.
 * <p>
 * Este analizador sintáctico se encarga de verificar que el programa cumpla con la gramática
 * definida en el archivo /grammar/factorized-grammar.bnf
 */
public class SyntacticAnalyzer extends AbstractSyntacticAnalyzer implements Syntactic {
    private SymbolTableHandler st;
    private AstHandler ast;

    public SyntacticAnalyzer(Lexical lexicalAnalyzer, SymbolTableHandler st, AstHandler ast) {
        super(lexicalAnalyzer);
        this.st = st;
        this.ast = ast;
    }

    /**
     * Llama por primera vez a next token y luego trata de analizar el programa
     *
     * @throws SyntacticException si ocurre un error sintactico
     * @throws LexicalException   si ocurre un error lexico
     */
    @Override
    public void analyze() throws SyntacticException, LexicalException, SemanticException {
        nextToken();
        program();
        st.consolidate();
    }

    @Override
    public String getSymbolTableJson() {
        return st.toJson();
    }

    /**
     * A partir de acá, cada método está asociado a un no terminal de la gramática
     * En la carpeta /grammar está la gramática final en formato BNF
     */
    private void program() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Lista-Definiciones⟩ ⟨Start⟩
        listaDefiniciones();
        start();
        match(Type.EOF);
    }

    private void start() throws SyntacticException, LexicalException, SemanticException {
        // start ⟨Bloque-Método⟩
        match(Type.KW_START);
        st.handleStart();
        bloqueMetodo();
    }

    private void listaDefiniciones() throws SyntacticException, LexicalException, SemanticException {
        Type[] follow = {Type.KW_START};
        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
        if (contains(follow)) {
            return;
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

    private void struct() throws SyntacticException, LexicalException, SemanticException {
        // struct idStruct ⟨Struct-O-Herencia⟩
        match(Type.KW_STRUCT);
        Token idClass = match(Type.ID_CLASS);
        st.handleNewClass(idClass);
        structOHerencia();
    }

    private void structOHerencia() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Herencia⟩ { ⟨Struct-Atributo⟩ } | { ⟨Struct-Atributo⟩ }

        // No terminal Herencia es opcional. No hay un metodo encargado de matchearlo
        if (getTokenType() == Type.COLON) {
            herencia();
        }

        match(Type.OPEN_CURLY);
        structAtributo();
        match(Type.CLOSE_CURLY);
    }

    private void structAtributo() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Atributo⟩ ⟨Struct-Atributo⟩ | λ

        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
        Type[] follow = {Type.CLOSE_CURLY};
        if (contains(follow)) {
            return;
        }

        atributo();
        structAtributo();
    }

    private void atributo() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Visibilidad⟩ ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ; | ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;

        // Visibilidad es opcional
        boolean isPrivate = false;
        if (getTokenType() == Type.KW_PRI) {
            match(Type.KW_PRI);
            isPrivate = true;
        }

        AttributeType type = tipo();
        List<Token> attributeTokens = listaDeclaracionVariables();

        for (Token attributeToken : attributeTokens) {
            st.handleNewAttribute(attributeToken, type, isPrivate);
        }

        match(Type.SEMICOLON);
    }

    private void impl() throws SyntacticException, LexicalException, SemanticException {
        // impl idStruct { ⟨Miembro⟩ ⟨Miembro-Opcional⟩ }
        match(Type.KW_IMPL);
        Token structToken = match(Type.ID_CLASS);
        st.handleNewImpl(structToken);
        match(Type.OPEN_CURLY);
        miembro();
        miembroOpcional();
        match(Type.CLOSE_CURLY);
        st.handleFinishImpl();
    }

    private void miembroOpcional() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Miembro⟩ ⟨Miembro-Opcional⟩ | λ

        // Siguientes de miembro opcional. Indica que el no terminal deriva lambda
        if (getTokenType() == Type.CLOSE_CURLY) {
            return;
        }

        miembro();
        miembroOpcional();
    }

    private void herencia() throws SyntacticException, LexicalException, SemanticException {
        // : ⟨Tipo⟩
        match(Type.COLON);
        AttributeType token = tipo();
        st.handleInheritance(token);
    }

    private void miembro() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Método⟩ | ⟨Constructor⟩
        if (getTokenType() == Type.DOT) {
            constructor();
            return;
        }

        metodo();
    }

    private void constructor() throws SyntacticException, LexicalException, SemanticException {
        // . ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩
        Token dotToken = match(Type.DOT);
        st.handleConstructor(dotToken);
        argumentosFormales();
        bloqueMetodo();
        st.handleFinishMethod();
    }

    private void metodo() throws SyntacticException, LexicalException, SemanticException {
        // st fn idMetAt ⟨Argumentos-Formales⟩ -⟩ ⟨Tipo-Método⟩ ⟨Bloque-Método⟩
        // | fn idMetAt ⟨Argumentos-Formales⟩ -⟩ ⟨Tipo-Método⟩ ⟨Bloque-Método⟩

        boolean isStatic = false;

        // Opcional: forma-metodo
        if (getTokenType() == Type.KW_ST) {
            isStatic = true;
            match(Type.KW_ST);
        }

        match(Type.KW_FN);
        Token methodToken = match(Type.ID);

        st.handleNewMethod(methodToken, isStatic);

        argumentosFormales();
        match(Type.ARROW);
        AttributeType type = tipoMetodo();

        st.setMethodReturn(type);

        bloqueMetodo();

        st.handleFinishMethod();
    }

    private void bloqueMetodo() throws SyntacticException, LexicalException, SemanticException {
        // { ⟨Decl-Var-Locales-Metodo⟩ ⟨Sentencia-Metodo⟩ }
        match(Type.OPEN_CURLY);
        declVariableLocalesMetodo();
        sentenciaMetodo();
        match(Type.CLOSE_CURLY);
    }

    private void declVariableLocalesMetodo() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Decl-Var-Locales⟩ ⟨Decl-Var-Locales-Metodo⟩ | λ
        Type[] first = {Type.ID_CLASS, Type.ARRAY, Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        if (contains(first)) {
            declaracionVariablesLocales();
            declVariableLocalesMetodo();
        }

        // Si no hay match, asumimos que deriva lambda
    }

    private void sentenciaMetodo() throws SyntacticException, LexicalException {
        // ⟨Sentencia⟩ ⟨Sentencia-Metodo⟩ | λ

        // Follow = }
        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
        Type[] follow = {Type.CLOSE_CURLY};
        if (contains(follow)) {
            return;
        }

        sentencia();
        sentenciaMetodo();
    }

    private void declaracionVariablesLocales() throws SyntacticException, LexicalException, SemanticException {
        // ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;
        AttributeType type = tipo();
        List<Token> attributeTokens = listaDeclaracionVariables();

        for (Token attributeToken : attributeTokens) {
            st.handleLocalVar(attributeToken, type);
        }

        match(Type.SEMICOLON);
    }

    private List<Token> listaDeclaracionVariables() throws SyntacticException, LexicalException, SemanticException {
        // idMetAt ⟨Lambda-O-Variables⟩
        Token attributeToken = match(Type.ID);

        List<Token> attributeTokens = new ArrayList<>();

        // Si el siguiente token no es una coma, asumimos que termino
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            attributeTokens = listaDeclaracionVariables();
        }

        // Agregar el atributo actual al comienzo de la lista
        attributeTokens.add(0, attributeToken);
        return attributeTokens;
    }

    private void argumentosFormales() throws SyntacticException, LexicalException, SemanticException {
        // ( ) | ( ⟨Lista-Argumentos-Formales⟩ )

        match(Type.OPEN_PAR);

        // Si el siguiente token es un CLOSE_PAR, entonces no hay argumentos formales
        if (getTokenType() == Type.CLOSE_PAR) {
            match(Type.CLOSE_PAR);
            return;
        }

        // De otro modo, intentar matchear los atributos formales
        listaArgumentosFormales(0);
        match(Type.CLOSE_PAR);
    }

    private void listaArgumentosFormales(int currentParamPosition) throws SyntacticException, LexicalException, SemanticException {
        // ⟨Argumento-Formal⟩ ⟨Argumento-Formal-O-Lambda⟩
        argumentoFormal(currentParamPosition);
        argumentoFormalOLambda(currentParamPosition);
    }

    private void argumentoFormal(int position) throws SyntacticException, LexicalException, SemanticException {
        // ⟨Tipo⟩ idMetAt
        AttributeType type = tipo();
        Token paramToken = match(Type.ID);
        st.addMethodParam(paramToken, type, position);
    }

    private void argumentoFormalOLambda(int currentParamPosition) throws SyntacticException, LexicalException, SemanticException {
        // , ⟨Lista-Argumentos-Formales⟩ | λ
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            listaArgumentosFormales(currentParamPosition + 1);
        }

        // Si el token no es una coma, asumimos que es lambda
    }


    private AttributeType tipoMetodo() throws SyntacticException, LexicalException {
        // El tipo de retorno de un método puede ser cualqueir tipo hallado en `tipo()`
        // O tambien puede ser void
        if (getTokenType() == Type.TYPE_VOID) {
            match(Type.TYPE_VOID);
            return null; // TODO: fix to avoid confuse with nil
        }

        return tipo();
    }

    private AttributeType tipo() throws SyntacticException, LexicalException {
        // Un tipo puede ser un ID_CLASS
        if (getTokenType() == Type.ID_CLASS) {
            Token id = match(Type.ID_CLASS);
            return new AttributeType(false, false, id);
        }

        // Tambien puede ser Array seguido de un tipo primitivo
        // O simplemente un tipo primitivo
        if (getTokenType() == Type.ARRAY) {
            match(Type.ARRAY);
            Token type = tipoPrimitivo();
            return new AttributeType(true, true, type);
        }

        Type[] primitive = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        if (contains(primitive)) {
            Token type = tipoPrimitivo();
            return new AttributeType(false, true, type);
        }

        // Lanzar error sintactico con TODOS los posibles tipos que podrían haber aparecido
        throwSyntacticException("tipo");
        return null;
    }

    private Token tipoPrimitivo() throws SyntacticException, LexicalException {
        Type[] first = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        return match(first);
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
        if (contains(first)) {
            asignacion();
            match(Type.SEMICOLON);
            return;
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
        throwSyntacticException("Sentencia");
    }

    private void expresionOSemicolon() throws SyntacticException, LexicalException {
        // ⟨Expresión⟩ ; | ;
        if (getTokenType() == Type.SEMICOLON) {
            match(Type.SEMICOLON);
            return;
        }

        expresion();
        match(Type.SEMICOLON);
    }

    private void bloque() throws SyntacticException, LexicalException {
        // { ⟨Sentencia-Bloque⟩ }
        match(Type.OPEN_CURLY);
        sentenciaBloque();
        match(Type.CLOSE_CURLY);
    }

    private void sentenciaBloque() throws SyntacticException, LexicalException {
        // ⟨Sentencia⟩ ⟨Sentencia-Bloque⟩ | λ
        Type[] first = {Type.SEMICOLON, Type.KW_RET, Type.KW_IF, Type.KW_WHILE, Type.KW_SELF, Type.OPEN_PAR, Type.OPEN_CURLY, Type.ID};
        if (contains(first)) {
            sentencia();
            sentenciaBloque();
        }

        // Otro caso, lambda
    }

    private void asignacion() throws SyntacticException, LexicalException {
        // ⟨AccesoVar-Simple⟩ = ⟨Expresión⟩
        if (getTokenType() == Type.ID) {
            PrimaryNode leftSide = accesoVarSimple();
            match(Type.ASSIGN);
            expresion();
            return;
        }

        // ⟨AccesoSelf-Simple⟩ ⟨Encadenado-Simple⟩ = ⟨Expresión⟩
        if (getTokenType() == Type.KW_SELF) {
            PrimaryNode leftSide = accesoSelfSimple();
            match(Type.ASSIGN);
            expresion();
            return;
        }

        throwSyntacticException("asignación");
    }

    private PrimaryNode accesoVarSimple() throws SyntacticException, LexicalException {
        // id ⟨Encadenado-O-Bracket⟩
        Token varToken = match(Type.ID);
        return encadenadoOBracket(varToken);
    }

    private PrimaryNode encadenadoOBracket(Token varToken) throws SyntacticException, LexicalException {
        // ⟨Encadenado-Simple⟩ | [ ⟨Expresión⟩ ]

        PrimaryNode parentNode;

        // [ ⟨Expresión⟩ ]
        if (getTokenType() == Type.OPEN_BRACKET) {
            match(Type.OPEN_BRACKET);
            ExpressionNode indexExpression = expresion();
            match(Type.CLOSE_BRACKET);
            parentNode = ast.createArrayAccess(varToken, indexExpression);
        }
        else {
            parentNode = ast.createVariableAccess(varToken,false);
        }

        // ⟨Encadenado-Simple⟩
        PrimaryNode childrenNode = encadenadosSimples(false);
        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private PrimaryNode encadenadosSimples(boolean isSelf) throws SyntacticException, LexicalException {
        // ⟨Encadenado-Simple⟩ ⟨Encadenados-Simples⟩ | λ
        if (getTokenType() == Type.DOT) {
            PrimaryNode parentNode = encadenadoSimple(isSelf);
            PrimaryNode chidlrenNode = encadenadosSimples(false);
            return ast.handlePossibleChain(parentNode, chidlrenNode);
        }

        return null;
    }

    private VariableAccessNode encadenadoSimple(boolean isSelf) throws SyntacticException, LexicalException {
        match(Type.DOT);
        Token varToken = match(Type.ID);
        return ast.createVariableAccess(varToken, isSelf);
    }

    private PrimaryNode accesoSelfSimple() throws SyntacticException, LexicalException {
        match(Type.KW_SELF);
        return encadenadosSimples(true);
    }

    private void sentenciaSimple() throws SyntacticException, LexicalException {
        // ( ⟨Expresión⟩ ) ;
        match(Type.OPEN_PAR);
        expresion();
        match(Type.CLOSE_PAR);
        match(Type.SEMICOLON);
    }

    private ExpressionNode expresion() throws SyntacticException, LexicalException {
        // ⟨Expresión⟩ ::= ⟨ExpOr⟩
        // ⟨ExpOr⟩ ::= ⟨ExpAnd⟩ ⟨ExpOr`⟩
        try {
            // ⟨ExpAnd⟩ ⟨ExpOr`⟩
            expAnd();
            expOrPrima();
        } catch (SyntacticException e) {
            throwSyntacticException("expresión");
        }
        return null; // TODO
    }

    private void expOrPrima() throws SyntacticException, LexicalException {
        // or ⟨ExpAnd⟩ ⟨ExpOr`⟩ | λ
        if (getTokenType() == Type.OR) {
            match(Type.OR);
            expAnd();
            expOrPrima();
        }
    }

    private void expAnd() throws SyntacticException, LexicalException {
        // ⟨ExpIgual⟩ ⟨ExpAnd`⟩
        expIgual();
        expAndPrima();
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

    private void expIgual() throws SyntacticException, LexicalException {
        // ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩
        expCompuesta();
        expIgualPrima();
    }

    private void expIgualPrima() throws SyntacticException, LexicalException {
        // ⟨OpIgual⟩ ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩ | λ
        Type[] opIgual = {Type.EQUAL, Type.NOT_EQUAL};
        if (contains(opIgual)) {
            opIgual();
            expCompuesta();
            expIgualPrima();
        }


        // Otro caso, lambda
    }

    private void expCompuesta() throws SyntacticException, LexicalException {
        // ⟨ExpAd⟩ ⟨ExpCompuesta`⟩
        expAd();
        expCompuestaPrima();
    }

    private void expCompuestaPrima() throws SyntacticException, LexicalException {
        // ⟨OpCompuesto⟩ ⟨ExpAd⟩ | λ
        Type[] opCompuesto = {Type.GREATER, Type.LESS, Type.GREATER_EQUAL, Type.LESS_EQUAL};
        if (contains(opCompuesto)) {
            opCompuesto();
            expAd();
        }
    }

    private void expAd() throws SyntacticException, LexicalException {
        // ⟨ExpMul⟩ ⟨ExpAd`⟩
        expMul();
        expAdPrima();
    }

    private void expAdPrima() throws SyntacticException, LexicalException {
        // ⟨OpAd⟩ ⟨ExpMul⟩ ⟨ExpAd'⟩ | λ
        Type[] opAd = {Type.PLUS, Type.MINUS};
        if (contains(opAd)) {
            opAd();
            expMul();
            expAdPrima();
        }
    }

    private void expMul() throws SyntacticException, LexicalException {
        // ⟨ExpUn⟩ ⟨ExpMul`⟩
        expUn();
        expMulPrima();
    }

    private void expMulPrima() throws SyntacticException, LexicalException {
        // ⟨OpMul⟩ ⟨ExpUn⟩ ⟨ExpMul`⟩ | λ
        Type[] opMul = {Type.MULT, Type.DIV, Type.MOD};
        if (contains(opMul)) {
            opMul();
            expUn();
            expMulPrima();
        }
    }

    private void expUn() throws SyntacticException, LexicalException {
        // ⟨OpUnario⟩ ⟨ExpUn⟩ | ⟨Operando⟩
        Type[] opUnario = {Type.PLUS, Type.MINUS, Type.NEG, Type.DPLUS, Type.DMINUS};
        if (contains(opUnario)) {
            opUnario();
            expUn();
            return;
        }

        // ⟨Operando⟩
        OperatingNode operating = operando();
    }

    private void opIgual() throws SyntacticException, LexicalException {
        Type[] opIgual = {Type.EQUAL, Type.NOT_EQUAL};
        match(opIgual);
    }

    private void opCompuesto() throws SyntacticException, LexicalException {
        Type[] opCompuesto = {Type.GREATER, Type.LESS, Type.GREATER_EQUAL, Type.LESS_EQUAL};
        match(opCompuesto);
    }

    private void opAd() throws SyntacticException, LexicalException {
        Type[] opAd = {Type.PLUS, Type.MINUS};
        match(opAd);
    }

    private void opUnario() throws SyntacticException, LexicalException {
        Type[] opUnario = {Type.PLUS, Type.MINUS, Type.NEG, Type.DPLUS, Type.DMINUS};
        match(opUnario);
    }

    private void opMul() throws SyntacticException, LexicalException {
        Type[] opMul = {Type.MULT, Type.DIV, Type.MOD};
        match(opMul);
    }

    private OperatingNode operando() throws SyntacticException, LexicalException {
        // nil | true | false | intLiteral | StrLiteral | charLiteral
        Type[] literals = {
                Type.KW_NIL,
                Type.KW_TRUE,
                Type.KW_FALSE,
                Type.INT_LITERAL,
                Type.STRING_LITERAL,
                Type.CHAR_LITERAL
        };
        if (contains(literals)) {
            Token literalToken = match(literals);
            return ast.createLiteral(literalToken);
        }

        // ⟨Primario⟩ ⟨Primarios⟩
        PrimaryNode parentNode = primario();
        PrimaryNode childrenNode = primarios();

        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private PrimaryNode primarios() throws SyntacticException, LexicalException {
        // ⟨Encadenado⟩ | λ
        if (getTokenType() == Type.DOT) {
            return encadenado(false);
        }

        return null;
    }

    private PrimaryNode primario() throws SyntacticException, LexicalException {
        // ⟨ExpresionParentizada⟩
        if (getTokenType() == Type.OPEN_PAR) {
            return expresionParentizada();
        }

        // ⟨AccesoSelf⟩
        // ⟨AccesoSelf⟩ ::= self ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.KW_SELF) {
            match(Type.KW_SELF);
            return encadenadoOLambda(true);
        }

        // id ⟨AccesoVar-O-Llamada-Método⟩
        if (getTokenType() == Type.ID) {
            Token varToken = match(Type.ID);
            return accesoVarOLLamadaMetodo(varToken);
        }

        //  ⟨Llamada-Método-Estático⟩
        if (getTokenType() == Type.ID_CLASS) {
            return  llamadaMetodoEstatico();
        }

        // ⟨Llamada-Constructor⟩
        if (getTokenType() == Type.KW_NEW) {
            match(Type.KW_NEW);
            return llamadaNew();
        }

        // Devolver error en otro caso
        throwSyntacticException("Primario");
        return null; // Unreachable line
    }

    private PrimaryNode accesoVarOLLamadaMetodo(Token varToken) throws SyntacticException, LexicalException {
        // ⟨Llamada-Método⟩
        if (getTokenType() == Type.OPEN_PAR) {
            MethodCallNode methodCall = ast.createMethodCallNode(varToken);
            llamadaMetodo(methodCall);

            return methodCall;
        }

        // ⟨AccesoVar⟩
        return accesoVar(varToken);
    }

    private PrimaryNode expresionParentizada() throws SyntacticException, LexicalException {
        // ( ⟨Expresion⟩ ) ⟨Encadenado-O-Lambda⟩
        match(Type.OPEN_PAR);
        ExpressionNode expressionNode = expresion();
        PrimaryNode parentNode = ast.createParentizedExpressionNode(expressionNode);
        match(Type.CLOSE_PAR);
        PrimaryNode childrenNode = encadenadoOLambda(false);
        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private PrimaryNode encadenadoOLambda(boolean isSelf) throws SyntacticException, LexicalException {
        // ⟨Encadenado⟩ | λ
        if (getTokenType() == Type.DOT) {
            return encadenado(isSelf);
        }
        return null;
    }

    private PrimaryNode accesoVar(Token varToken) throws SyntacticException, LexicalException {
        PrimaryNode parentNode;

        // [ ⟨Expresión⟩ ] ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.OPEN_BRACKET) {
            match(Type.OPEN_BRACKET);
            ExpressionNode indexExpression = expresion();
            match(Type.CLOSE_BRACKET);
            parentNode = ast.createArrayAccess(varToken, indexExpression);
        }
        else {
            parentNode = ast.createVariableAccess(varToken, false);
        }

        // ⟨Encadenado-O-Lambda⟩
        PrimaryNode childrenNode = encadenadoOLambda(false);

        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private void llamadaMetodo(MethodCall methodCall) throws SyntacticException, LexicalException {
        //  ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        // Create a new MethodCallNode with the varToken
        argumentosActuales(methodCall);
        encadenadoOLambda(false);
    }

    private StaticMethodCallNode llamadaMetodoEstatico() throws SyntacticException, LexicalException {
        // idStruct . id ⟨Llamada-Método⟩ ⟨Encadenado-O-Lambda⟩
        Token varClass = match(Type.ID_CLASS);
        match(Type.DOT);
        Token varToken = match(Type.ID);
        StaticMethodCallNode staticMethodCallNode = ast.createStaticMethodCallNode(varClass, varToken);
        llamadaMetodo(staticMethodCallNode);
        encadenadoOLambda(false);

        return staticMethodCallNode;
    }

    private PrimaryNode llamadaNew() throws SyntacticException, LexicalException {
        // idStruct ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.ID_CLASS) {
            Token classToken = match(Type.ID_CLASS);
            ConstructorCallNode constructor = ast.createConstructorCallNode(classToken);
            argumentosActuales(constructor);
            encadenadoOLambda(false);
            return constructor;
        }

        // ⟨Tipo-Primitivo⟩ [ ⟨Expresion⟩ ]
        Type[] primitive = {Type.TYPE_INT, Type.TYPE_CHAR, Type.TYPE_STRING, Type.TYPE_BOOL};
        if (contains(primitive)) {
            Token elementsTypeToken = tipoPrimitivo();
            match(Type.OPEN_BRACKET);
            ExpressionNode expressionNode = expresion();
            match(Type.CLOSE_BRACKET);
            return ast.createNewArrayNode(elementsTypeToken, expressionNode);
        }

        throwSyntacticException("Tipo primitivo o IdClass");
        return null; // Unreachable line
    }

    private void argumentosActuales(MethodCall method) throws SyntacticException, LexicalException {
        // ( ⟨Lista-Expresiones⟩ ) | ( )
        match(Type.OPEN_PAR);

        if (getTokenType() == Type.CLOSE_PAR) {
            match(Type.CLOSE_PAR);
        }

        else {
            List<ExpressionNode> params = listaExpresiones();
            method.setParameters(params);
            match(Type.CLOSE_PAR);
        }
    }

    private List<ExpressionNode> listaExpresiones() throws SyntacticException, LexicalException {
        List<ExpressionNode> expressionNodeList = new ArrayList<>();

        // ⟨Expresión⟩ ⟨Expresiones⟩
        ExpressionNode expressionNode = expresion();

        // ⟨Expresiones⟩ ::= λ | , ⟨Lista-Expresiones⟩
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            expressionNodeList = listaExpresiones();
        }

        expressionNodeList.add(expressionNode);

        return expressionNodeList;
    }

    private PrimaryNode encadenado(boolean isSelf) throws SyntacticException, LexicalException {
        // . id ⟨Llamada-Método-Encadenado-O-AccesoVar⟩
        // ⟨Llamada-Método-Encadenado-O-AccesoVar⟩ ::= ⟨Llamada-Método-Encadenado⟩ | ⟨AccesoVar⟩

        match(Type.DOT);
        Token varToken = match(Type.ID);

        // ⟨AccesoVar⟩
        Type[] first = {Type.DOT, Type.OPEN_BRACKET};

        if (contains(first)) {
            return accesoVar(varToken);
        }

        // ⟨Llamada-Método-Encadenado⟩ ::= ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.OPEN_PAR) {
            MethodCallNode method = new MethodCallNode(varToken);
            argumentosActuales(method);

            if (getTokenType() == Type.DOT) {
                PrimaryNode childrenNode = encadenado(false);
                return  ast.handlePossibleChain(method, childrenNode);
            }

            return method;
        }

        return ast.createVariableAccess(varToken, isSelf);
    }
}
