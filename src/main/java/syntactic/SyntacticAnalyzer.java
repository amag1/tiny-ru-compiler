package syntactic;

import codeGeneration.CodeGenerator;
import exceptions.lexical.LexicalException;
import exceptions.semantic.symbolTable.SymbolTableException;
import exceptions.semantic.syntaxTree.AstException;
import exceptions.syntactic.SyntacticException;
import lexical.Lexical;
import lexical.Token;
import lexical.Type;
import semantic.abstractSintaxTree.AstHandler;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Sentence.AssignationNode;
import semantic.abstractSintaxTree.Sentence.BlockNode;
import semantic.abstractSintaxTree.Sentence.ReturnNode;
import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableHandler;

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
    public CodeGenerator analyze() throws SyntacticException, LexicalException, SymbolTableException, AstException {
        nextToken();
        program();
        st.consolidate();
        ast.validateSenteces();

        return new CodeGenerator(ast.getAst(), st.getSymbolTable());
    }

    @Override
    public String getSymbolTableJson() {
        return st.toJson();
    }

    public String getAbstractSybolTreeJson() {
        return ast.toJson();
    }

    /**
     * A partir de acá, cada método está asociado a un no terminal de la gramática
     * En la carpeta /grammar está la gramática final en formato BNF
     */
    private void program() throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Lista-Definiciones⟩ ⟨Start⟩
        listaDefiniciones();
        start();
        match(Type.EOF);
    }

    private void start() throws SyntacticException, LexicalException, SymbolTableException {
        // start ⟨Bloque-Método⟩
        Token start = match(Type.ID);
        if (!start.getLexem().equals("start")) {
            throwSyntacticException("start", start);
        }
        st.handleStart();
        ast.createMethodIfNotExists(start);
        bloqueMetodo();
    }

    private void listaDefiniciones() throws SyntacticException, LexicalException, SymbolTableException {
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

    private void struct() throws SyntacticException, LexicalException, SymbolTableException {
        // struct idStruct ⟨Struct-O-Herencia⟩
        match(Type.KW_STRUCT);
        Token idClass = match(Type.ID_CLASS);
        st.handleNewClass(idClass);
        structOHerencia();
    }

    private void structOHerencia() throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Herencia⟩ { ⟨Struct-Atributo⟩ } | { ⟨Struct-Atributo⟩ }

        // No terminal Herencia es opcional. No hay un metodo encargado de matchearlo
        if (getTokenType() == Type.COLON) {
            herencia();
        }

        match(Type.OPEN_CURLY);
        structAtributo();
        match(Type.CLOSE_CURLY);
    }

    private void structAtributo() throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Atributo⟩ ⟨Struct-Atributo⟩ | λ

        // Cuando un no terminal deriva lambda, se chequea si el token actual es uno de los siguientes
        Type[] follow = {Type.CLOSE_CURLY};
        if (contains(follow)) {
            return;
        }

        atributo();
        structAtributo();
    }

    private void atributo() throws SyntacticException, LexicalException, SymbolTableException {
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

    private void impl() throws SyntacticException, LexicalException, SymbolTableException {
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

    private void miembroOpcional() throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Miembro⟩ ⟨Miembro-Opcional⟩ | λ

        // Siguientes de miembro opcional. Indica que el no terminal deriva lambda
        if (getTokenType() == Type.CLOSE_CURLY) {
            return;
        }

        miembro();
        miembroOpcional();
    }

    private void herencia() throws SyntacticException, LexicalException, SymbolTableException {
        // : ⟨Tipo⟩
        match(Type.COLON);
        AttributeType token = tipo();
        st.handleInheritance(token);
    }

    private void miembro() throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Método⟩ | ⟨Constructor⟩
        if (getTokenType() == Type.DOT) {
            constructor();
            return;
        }

        if (getTokenType() == Type.KW_FN || getTokenType() == Type.KW_ST) {
            metodo();
            return;
        }

        throwSyntacticException("constructor o método");
    }

    private void constructor() throws SyntacticException, LexicalException, SymbolTableException {
        // . ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩
        Token dotToken = match(Type.DOT);
        st.handleConstructor(dotToken);
        argumentosFormales();
        bloqueMetodo();
        st.handleFinishMethod();
    }

    private void metodo() throws SyntacticException, LexicalException, SymbolTableException {
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
        ast.createMethodIfNotExists(methodToken);

        argumentosFormales();
        match(Type.ARROW);
        AttributeType type = tipoMetodo();

        st.setMethodReturn(type);

        bloqueMetodo();

        st.handleFinishMethod();
    }

    private void bloqueMetodo() throws SyntacticException, LexicalException, SymbolTableException {
        // { ⟨Decl-Var-Locales-Metodo⟩ ⟨Sentencia-Metodo⟩ }
        match(Type.OPEN_CURLY);
        declVariableLocalesMetodo();
        sentenciaMetodo();
        match(Type.CLOSE_CURLY);
    }

    private void declVariableLocalesMetodo() throws SyntacticException, LexicalException, SymbolTableException {
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

        SentenceNode sentence = sentencia();
        ast.addSentence(sentence);
        sentenciaMetodo();
    }

    private void declaracionVariablesLocales() throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;
        AttributeType type = tipo();
        List<Token> attributeTokens = listaDeclaracionVariables();

        for (Token attributeToken : attributeTokens) {
            st.handleLocalVar(attributeToken, type);
        }

        match(Type.SEMICOLON);
    }

    private List<Token> listaDeclaracionVariables() throws SyntacticException, LexicalException, SymbolTableException {
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

    private void argumentosFormales() throws SyntacticException, LexicalException, SymbolTableException {
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

    private void listaArgumentosFormales(int currentParamPosition) throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Argumento-Formal⟩ ⟨Argumento-Formal-O-Lambda⟩
        argumentoFormal(currentParamPosition);
        argumentoFormalOLambda(currentParamPosition);
    }

    private void argumentoFormal(int position) throws SyntacticException, LexicalException, SymbolTableException {
        // ⟨Tipo⟩ idMetAt
        AttributeType type = tipo();
        Token paramToken = match(Type.ID);
        st.addMethodParam(paramToken, type, position);
    }

    private void argumentoFormalOLambda(int currentParamPosition) throws SyntacticException, LexicalException, SymbolTableException {
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
            return null; // Void
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

    private SentenceNode sentencia() throws SyntacticException, LexicalException {
        // ;
        if (getTokenType() == Type.SEMICOLON) {
            match(Type.SEMICOLON);
            return ast.createEmptySentenceNode();
        }

        // ret ⟨Expresion-O-Semicolon⟩
        if (getTokenType() == Type.KW_RET) {
            Token returnToken = match(Type.KW_RET);
            return expresionOSemicolon(returnToken);
        }

        // if ( ⟨Expresión⟩ ) ⟨Sentencia⟩ ⟨Else-O-Lambda⟩
        if (getTokenType() == Type.KW_IF) {
            match(Type.KW_IF);
            match(Type.OPEN_PAR);
            ExpressionNode condition = expresion();
            match(Type.CLOSE_PAR);
            SentenceNode ifSentence = sentencia();

            // else ⟨Sentencia⟩ | λ
            SentenceNode elseSentence = null;
            if (getTokenType() == Type.KW_ELSE) {
                match(Type.KW_ELSE);
                elseSentence = sentencia();
            }

            return ast.createIfElseNode(condition, ifSentence, elseSentence);
        }

        // while ( ⟨Expresión⟩ ) ⟨Sentencia⟩
        if (getTokenType() == Type.KW_WHILE) {
            match(Type.KW_WHILE);
            match(Type.OPEN_PAR);
            ExpressionNode condition = expresion();
            match(Type.CLOSE_PAR);
            SentenceNode body = sentencia();
            return ast.createWhileNode(condition, body);
        }

        // ⟨Asignación⟩ ;
        Type[] first = {Type.ID, Type.KW_SELF};
        if (contains(first)) {
            AssignationNode assignation = asignacion();
            match(Type.SEMICOLON);
            return assignation;
        }


        // ⟨Sentencia-Simple⟩
        if (getTokenType() == Type.OPEN_PAR) {
            return sentenciaSimple();
        }

        // ⟨Bloque⟩
        if (getTokenType() == Type.OPEN_CURLY) {
            return bloque();
        }

        // Devolver error en caso de no matchear ninguno de los anteriores
        throwSyntacticException("Sentencia");
        return null;
    }

    private ReturnNode expresionOSemicolon(Token returnToken) throws SyntacticException, LexicalException {
        // ⟨Expresión⟩ ; | ;
        if (getTokenType() == Type.SEMICOLON) {
            match(Type.SEMICOLON);
            return ast.createEmptyReturnNode(returnToken);
        }

        ExpressionNode ret = expresion();
        match(Type.SEMICOLON);

        return ast.createReturnNode(ret, returnToken);
    }

    private BlockNode bloque() throws SyntacticException, LexicalException {
        // { ⟨Sentencia-Bloque⟩ }
        match(Type.OPEN_CURLY);
        List<SentenceNode> sentences = sentenciaBloque();
        match(Type.CLOSE_CURLY);

        return ast.createBlockNode(sentences);
    }

    private List<SentenceNode> sentenciaBloque() throws SyntacticException, LexicalException {
        // ⟨Sentencia⟩ ⟨Sentencia-Bloque⟩ | λ
        Type[] first = {Type.SEMICOLON, Type.KW_RET, Type.KW_IF, Type.KW_WHILE, Type.KW_SELF, Type.OPEN_PAR, Type.OPEN_CURLY, Type.ID};
        if (contains(first)) {
            List<SentenceNode> sentences = new ArrayList<>();
            SentenceNode sentence = sentencia();
            sentences.add(sentence);

            List<SentenceNode> recursiveSentences = sentenciaBloque();

            sentences.addAll(recursiveSentences);
            return sentences;
        }

        return new ArrayList<>();
        // Otro caso, lambda
    }

    private AssignationNode asignacion() throws SyntacticException, LexicalException {
        // ⟨AccesoVar-Simple⟩ = ⟨Expresión⟩
        if (getTokenType() == Type.ID) {
            PrimaryNode leftSide = accesoVarSimple();
            match(Type.ASSIGN);
            ExpressionNode rightSide = expresion();
            return ast.createAssignationNode(leftSide, rightSide);
        }

        // ⟨AccesoSelf-Simple⟩ ⟨Encadenado-Simple⟩ = ⟨Expresión⟩
        if (getTokenType() == Type.KW_SELF) {
            PrimaryNode leftSide = accesoSelfSimple();
            match(Type.ASSIGN);
            ExpressionNode rightSide = expresion();
            return ast.createAssignationNode(leftSide, rightSide);
        }

        throwSyntacticException("asignación");
        return null;
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
            parentNode = ast.createVariableAccess(varToken);
        }

        // ⟨Encadenado-Simple⟩
        PrimaryNode childrenNode = encadenadosSimples();
        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private PrimaryNode encadenadosSimples() throws SyntacticException, LexicalException {
        // ⟨Encadenado-Simple⟩ ⟨Encadenados-Simples⟩ | λ
        if (getTokenType() == Type.DOT) {
            PrimaryNode parentNode = encadenadoSimple();
            PrimaryNode chidlrenNode = encadenadosSimples();
            return ast.handlePossibleChain(parentNode, chidlrenNode);
        }

        return null;
    }

    private VariableAccessNode encadenadoSimple() throws SyntacticException, LexicalException {
        match(Type.DOT);
        Token varToken = match(Type.ID);
        return ast.createVariableAccess(varToken);
    }

    private PrimaryNode accesoSelfSimple() throws SyntacticException, LexicalException {
        Token selfKeyword = match(Type.KW_SELF);
        PrimaryNode node = encadenadosSimples();
        return ast.createSelfAccess(node, selfKeyword);
    }

    private SentenceNode sentenciaSimple() throws SyntacticException, LexicalException {
        // ( ⟨Expresión⟩ ) ;
        match(Type.OPEN_PAR);
        ExpressionNode expression = expresion();
        match(Type.CLOSE_PAR);
        match(Type.SEMICOLON);

        return ast.createSimpleSentenceNode(expression);
    }

    private ExpressionNode expresion() throws SyntacticException, LexicalException {
        // ⟨Expresión⟩ ::= ⟨ExpOr⟩
        // ⟨ExpOr⟩ ::= ⟨ExpAnd⟩ ⟨ExpOr`⟩
        try {
            // ⟨ExpAnd⟩ ⟨ExpOr`⟩
            ExpressionNode expression = expAnd();
            ExpressionNode or = expOrPrima(expression);
            return or;
        } catch (SyntacticException e) {
            throwSyntacticException("expresión");
        }
        return null; // Unreachable line
    }

    private ExpressionNode expOrPrima(ExpressionNode previousExpression) throws SyntacticException, LexicalException {
        // or ⟨ExpAnd⟩ ⟨ExpOr`⟩ | λ
        if (getTokenType() == Type.OR) {
            Token or = match(Type.OR);
            ExpressionNode expression = expAnd();
            ExpressionNode recursiveExpression = expOrPrima(expression);

            return ast.createBinaryOperationNode(or, previousExpression, recursiveExpression);

        }

        return previousExpression;
    }

    private ExpressionNode expAnd() throws SyntacticException, LexicalException {
        // ⟨ExpIgual⟩ ⟨ExpAnd`⟩
        ExpressionNode expression = expIgual();
        ExpressionNode and = expAndPrima(expression);

        return and;
    }

    private ExpressionNode expAndPrima(ExpressionNode previousExpression) throws SyntacticException, LexicalException {
        // && ⟨ExpIgual⟩ ⟨ExpAnd`⟩ | λ
        if (getTokenType() == Type.AND) {
            Token and = match(Type.AND);
            ExpressionNode expression = expIgual();
            ExpressionNode recursiveExpression = expAdPrima(expression);

            return ast.createBinaryOperationNode(and, previousExpression, recursiveExpression);
        }

        // Otro caso, lambda
        return previousExpression;
    }

    private ExpressionNode expIgual() throws SyntacticException, LexicalException {
        // ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩
        ExpressionNode expression = expCompuesta();
        ExpressionNode equal = expIgualPrima(expression);

        return equal;
    }

    private ExpressionNode expIgualPrima(ExpressionNode previousExpression) throws SyntacticException, LexicalException {
        // ⟨OpIgual⟩ ⟨ExpCompuesta⟩ ⟨ExpIgual`⟩ | λ
        Type[] opIgual = {Type.EQUAL, Type.NOT_EQUAL};
        if (contains(opIgual)) {
            Token operator = opIgual();
            ExpressionNode expression = expCompuesta();
            ExpressionNode recursiveExpression = expIgualPrima(expression);

            return ast.createBinaryOperationNode(operator, previousExpression, recursiveExpression);
        }

        return previousExpression;
        // Otro caso, lambda
    }

    private ExpressionNode expCompuesta() throws SyntacticException, LexicalException {
        // ⟨ExpAd⟩ ⟨ExpCompuesta`⟩
        ExpressionNode previousExpression = expAd();
        ExpressionNode compound = expCompuestaPrima(previousExpression);

        return compound;
    }

    private ExpressionNode expCompuestaPrima(ExpressionNode previousExpression) throws SyntacticException, LexicalException {
        // ⟨OpCompuesto⟩ ⟨ExpAd⟩ | λ
        Type[] opCompuesto = {Type.GREATER, Type.LESS, Type.GREATER_EQUAL, Type.LESS_EQUAL};
        if (contains(opCompuesto)) {
            Token operator = opCompuesto();
            ExpressionNode expression = expAd();

            return ast.createBinaryOperationNode(operator, previousExpression, expression);
        }

        return previousExpression;
    }

    private ExpressionNode expAd() throws SyntacticException, LexicalException {
        // ⟨ExpMul⟩ ⟨ExpAd`⟩
        ExpressionNode expression = expMul();
        ExpressionNode add = expAdPrima(expression);

        return add;
    }

    private ExpressionNode expAdPrima(ExpressionNode previousExpression) throws SyntacticException, LexicalException {
        // ⟨OpAd⟩ ⟨ExpMul⟩ ⟨ExpAd'⟩ | λ
        Type[] opAd = {Type.PLUS, Type.MINUS};
        if (contains(opAd)) {
            Token operator = opAd();
            ExpressionNode firstExpression = expMul();
            ExpressionNode recursiveExpression = expAdPrima(firstExpression);

            return ast.createBinaryOperationNode(operator, previousExpression, recursiveExpression);
        }

        return previousExpression;
    }

    private ExpressionNode expMul() throws SyntacticException, LexicalException {
        // ⟨ExpUn⟩ ⟨ExpMul`⟩
        ExpressionNode unaryExpression = expUn();
        // Usa el nodo unario para construir un arbol de expresion binaria
        ExpressionNode mult = expMulPrima(unaryExpression);

        // Retorna solo el arbol, se asume que la expresion unaria esta dentro del mismo
        return mult;
    }

    private ExpressionNode expMulPrima(ExpressionNode operating) throws SyntacticException, LexicalException {
        // ⟨OpMul⟩ ⟨ExpUn⟩ ⟨ExpMul`⟩ | λ
        Type[] opMul = {Type.MULT, Type.DIV, Type.MOD};
        if (contains(opMul)) {
            Token operator = opMul();
            ExpressionNode firstExpression = expUn();
            ExpressionNode recursiveExpression = expMulPrima(firstExpression);

            return ast.createBinaryOperationNode(operator, operating, recursiveExpression);
        }

        return operating;
    }

    private ExpressionNode expUn() throws SyntacticException, LexicalException {
        // ⟨OpUnario⟩ ⟨ExpUn⟩ | ⟨Operando⟩
        Type[] opUnario = {Type.PLUS, Type.MINUS, Type.NEG, Type.DPLUS, Type.DMINUS};
        if (contains(opUnario)) {
            Token operator = opUnario();
            ExpressionNode expression = expUn();
            return ast.createUnaryExpressionNode(operator, expression);
        }

        // ⟨Operando⟩
        OperatingNode operating = operando();

        return operating;
    }

    private Token opIgual() throws SyntacticException, LexicalException {
        Type[] opIgual = {Type.EQUAL, Type.NOT_EQUAL};
        return match(opIgual);
    }

    private Token opCompuesto() throws SyntacticException, LexicalException {
        Type[] opCompuesto = {Type.GREATER, Type.LESS, Type.GREATER_EQUAL, Type.LESS_EQUAL};
        return match(opCompuesto);
    }

    private Token opAd() throws SyntacticException, LexicalException {
        Type[] opAd = {Type.PLUS, Type.MINUS};
        return match(opAd);
    }

    private Token opUnario() throws SyntacticException, LexicalException {
        Type[] opUnario = {Type.PLUS, Type.MINUS, Type.NEG, Type.DPLUS, Type.DMINUS};
        return match(opUnario);
    }

    private Token opMul() throws SyntacticException, LexicalException {
        Type[] opMul = {Type.MULT, Type.DIV, Type.MOD};
        return match(opMul);
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
            return encadenado();
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
            Token selfKeyword = match(Type.KW_SELF);
            PrimaryNode node = encadenadoOLambda();
            return ast.createSelfAccess(node, selfKeyword);
        }

        // id ⟨AccesoVar-O-Llamada-Método⟩
        if (getTokenType() == Type.ID) {
            Token varToken = match(Type.ID);
            return accesoVarOLLamadaMetodo(varToken);
        }

        //  ⟨Llamada-Método-Estático⟩
        if (getTokenType() == Type.ID_CLASS) {
            return llamadaMetodoEstatico();
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
            PrimaryNode primary = llamadaMetodo(methodCall);

            return primary;
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
        PrimaryNode childrenNode = encadenadoOLambda();
        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private PrimaryNode encadenadoOLambda() throws SyntacticException, LexicalException {
        // ⟨Encadenado⟩ | λ
        if (getTokenType() == Type.DOT) {
            return encadenado();
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
            parentNode = ast.createVariableAccess(varToken);
        }

        // ⟨Encadenado-O-Lambda⟩
        PrimaryNode childrenNode = encadenadoOLambda();

        return ast.handlePossibleChain(parentNode, childrenNode);
    }

    private PrimaryNode llamadaMetodo(CallableNode methodCall) throws SyntacticException, LexicalException {
        //  ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        // Create a new MethodCallNode with the varToken
        List<ExpressionNode> params = argumentosActuales();
        ast.SetMethodParameter(methodCall, params);
        PrimaryNode childrenNode = encadenadoOLambda();
        return ast.handlePossibleChain(methodCall, childrenNode);
    }

    private PrimaryNode llamadaMetodoEstatico() throws SyntacticException, LexicalException {
        // idStruct . id ⟨Llamada-Método⟩ ⟨Encadenado-O-Lambda⟩
        Token varClass = match(Type.ID_CLASS);
        match(Type.DOT);
        Token varToken = match(Type.ID);
        StaticMethodCallNode staticMethodCallNode = ast.createStaticMethodCallNode(varClass, varToken);
        PrimaryNode primary = llamadaMetodo(staticMethodCallNode);
        PrimaryNode childrenNode = encadenadoOLambda();

        return ast.handlePossibleChain(primary, childrenNode);
    }

    private PrimaryNode llamadaNew() throws SyntacticException, LexicalException {
        // idStruct ⟨Argumentos-Actuales⟩ ⟨Encadenado-O-Lambda⟩
        if (getTokenType() == Type.ID_CLASS) {
            Token classToken = match(Type.ID_CLASS);
            ConstructorCallNode constructor = ast.createConstructorCallNode(classToken);
            List<ExpressionNode> params = argumentosActuales();
            ast.SetMethodParameter(constructor, params);
            PrimaryNode childrenNode = encadenadoOLambda();
            return ast.handlePossibleChain(constructor, childrenNode);
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

    private List<ExpressionNode> argumentosActuales() throws SyntacticException, LexicalException {
        // ( ⟨Lista-Expresiones⟩ ) | ( )
        match(Type.OPEN_PAR);

        if (getTokenType() == Type.CLOSE_PAR) {
            match(Type.CLOSE_PAR);
            return new ArrayList<>();
        }

        else {
            List<ExpressionNode> params = listaExpresiones();
            match(Type.CLOSE_PAR);
            return params;
        }
    }

    private List<ExpressionNode> listaExpresiones() throws SyntacticException, LexicalException {
        List<ExpressionNode> expressionNodeList = new ArrayList<>();

        // ⟨Expresión⟩ ⟨Expresiones⟩
        ExpressionNode expressionNode = expresion();
        expressionNodeList.add(expressionNode);

        // ⟨Expresiones⟩ ::= λ | , ⟨Lista-Expresiones⟩
        if (getTokenType() == Type.COMMA) {
            match(Type.COMMA);
            expressionNodeList.addAll(listaExpresiones());
        }


        return expressionNodeList;
    }

    private PrimaryNode encadenado() throws SyntacticException, LexicalException {
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
            List<ExpressionNode> params = argumentosActuales();
            ast.SetMethodParameter(method, params);

            if (getTokenType() == Type.DOT) {
                PrimaryNode childrenNode = encadenado();
                return ast.handlePossibleChain(method, childrenNode);
            }

            return method;
        }

        return ast.createVariableAccess(varToken);
    }
}
