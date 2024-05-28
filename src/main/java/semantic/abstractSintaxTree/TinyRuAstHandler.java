package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Sentence.*;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;
import semantic.symbolTable.SymbolTableLookup;

import java.util.List;
import java.util.Map;

/**
 * Clase que se encarga de manejar el árbol de sintaxis abstracta
 * y de crear los nodos necesarios para el AST
 * <p>
 * Posee metodos para crear nodos y otras estructuras del AST, y consultar
 * la tabla de simbolos
 */
public class TinyRuAstHandler implements AstHandler {

    private AbstractSyntaxTree ast;
    private SymbolTableLookup stHandler;

    public TinyRuAstHandler(SymbolTableLookup stHandler) {
        this.ast = new AbstractSyntaxTree();
        this.stHandler = stHandler;
    }

    /**
     * Valida todas las sentencias del AST
     *
     * @throws AstException Lanza una excepcion si alguna sentencia no es valida
     */
    public void validateSenteces() throws AstException {
        for (Map.Entry<String, AstClassEntry> entry : ast.getClasses().entrySet()) {
            AstClassEntry currentClass = entry.getValue();
            Context classContext = new Context(stHandler, currentClass.getName());
            currentClass.validateSentences(classContext);
        }

        // Validar sentencias del start también
        Context startContext = new Context(stHandler);
        ast.getStart().validateSentences(startContext);
    }

    public LiteralNode createLiteral(Token token) {
        return new LiteralNode(token);
    }

    public VariableAccessNode createVariableAccess(Token token) {
        return new VariableAccessNode(token);
    }

    @Override
    public ArrayAccessNode createArrayAccess(Token token, ExpressionNode indexExpression) {
        return new ArrayAccessNode(token, indexExpression);
    }

    public PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode) {
        if (childrenNode == null) {
            return parentNode;
        }
        else {
            return new ChainNode(parentNode, childrenNode);
        }
    }

    public MethodCallNode createMethodCallNode(Token methodToken) {
        return new MethodCallNode(methodToken);
    }

    public ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression) {
        return new ParentizedExpressionNode(expression);
    }

    public ConstructorCallNode createConstructorCallNode(Token classToken) {
        return new ConstructorCallNode(classToken);
    }

    public NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        return new NewArrayNode(elementsTypeToken, lengthExpression);
    }

    public StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken) {
        return new StaticMethodCallNode(classToken, methodToken);
    }

    public SelfAccess createSelfAccess(PrimaryNode node, Token selfKeyword) {
        return new SelfAccess(node, selfKeyword);
    }

    public UnaryOperationNode createUnaryExpressionNode(Token operator, ExpressionNode expression) {
        return new UnaryOperationNode(expression, operator);
    }

    public BinaryOperationNode createBinaryOperationNode(Token operator, ExpressionNode left, ExpressionNode right) {
        return new BinaryOperationNode(operator, left, right);
    }

    public void SetMethodParameter(CallableNode method, List<ExpressionNode> parameters) {
        method.setParameters(parameters);
    }

    public SimpleSentenceNode createSimpleSentenceNode(ExpressionNode expression) {
        return new SimpleSentenceNode(expression);
    }

    public ReturnNode createEmptyReturnNode(Token token) {
        return new ReturnNode(token);
    }

    public ReturnNode createReturnNode(ExpressionNode exp, Token token) {
        return new ReturnNode(exp, token);
    }

    public EmptySentenceNode createEmptySentenceNode() {
        return new EmptySentenceNode();
    }

    public IfElseNode createIfElseNode(ExpressionNode condition, SentenceNode ifSentence, SentenceNode elseSentence) {
        return new IfElseNode(condition, ifSentence, elseSentence);
    }

    public WhileNode createWhileNode(ExpressionNode condition, SentenceNode sentence) {
        return new WhileNode(condition, sentence);
    }

    public AssignationNode createAssignationNode(PrimaryNode leftSide, ExpressionNode rightNode) {
        return new AssignationNode(leftSide, rightNode);
    }

    public BlockNode createBlockNode(List<SentenceNode> sentences) {
        return new BlockNode(sentences);
    }

    /**
     * Agrega una sentencia al metodo actual
     *
     * @param sentence La sentencia a agregar
     */
    public void addSentence(SentenceNode sentence) {
        // Obtener la clase actual de la tabla de símbolos
        ClassEntry currentClass = stHandler.getCurrentClass();
        if (currentClass != null) {
            // Obtener o crear la clase en el ast
            AstClassEntry currentAstClass = getOrCreateAstClassEntry(currentClass);

            // Obtener o crear un nuevo método
            AstMethodEntry currentAstMethod = getOrCreateMethodEntry(currentAstClass);

            currentAstMethod.addSentence(sentence);
        }
        else {
            // Agregar al método start
            AstMethodEntry startMethod = ast.getStart();
            startMethod.addSentence(sentence);
        }
    }

    public void createMethodIfNotExists(Token methodName) {
        // Obtener la clase actual de la tabla de símbolos
        ClassEntry currentClass = stHandler.getCurrentClass();
        if (currentClass != null) {
            // Obtener o crear la clase en el ast
            AstClassEntry currentAstClass = getOrCreateAstClassEntry(currentClass);

            // Obtener o crear un nuevo método
            AstMethodEntry currentAstMethod = currentAstClass.getMethod(methodName.getLexem());
            if (currentAstMethod == null) {
                currentAstMethod = new AstMethodEntry(methodName);
                currentAstClass.addMethod(currentAstMethod);
            }
        }
        else {
            // Crear el método start
            if (ast.getStart() == null) {
                ast.setStart(new AstMethodEntry(methodName));
            }
        }
    }

    @Override
    public AbstractSyntaxTree getAst() {
        return ast;
    }

    private AstMethodEntry getOrCreateMethodEntry(AstClassEntry currentAstClass) {
        // El metodo existe en la tabla de simbolos porque ya leimos su firma
        MethodEntry currentMethod = stHandler.getCurrentMethod();

        // Si aun no existe en el AST, crearlo

        // Primero revisar si el metodo es el constructor
        if (currentMethod.getName().equals(".")) {
            if (currentAstClass.getConstructor() == null) {
                AstMethodEntry constructor = new AstMethodEntry(currentMethod.getToken());
                currentAstClass.setConstructor(constructor);
            }
            return currentAstClass.getConstructor();
        }

        // Si no es el constructor, buscar el mtodo con ese nombre
        AstMethodEntry currentAstMethod = currentAstClass.getMethod(currentMethod.getName());
        if (currentAstMethod == null) {
            currentAstMethod = new AstMethodEntry(currentMethod.getToken());

            // Si el metodo es el constructor, asignarlo a la clase
            if (currentAstMethod.name.equals(".")) {
                currentAstClass.setConstructor(currentAstMethod);
            }
            else {
                currentAstClass.addMethod(currentAstMethod);
            }
        }
        return currentAstMethod;
    }

    private AstClassEntry getOrCreateAstClassEntry(ClassEntry currentClass) {
        AstClassEntry currentAstClass = ast.getClass(currentClass.getName());
        if (currentAstClass == null) {
            currentAstClass = new AstClassEntry(currentClass.getName());
            ast.addClass(currentAstClass);
        }
        return currentAstClass;
    }

    public String toJson() {
        return ast.toJson();
    }
}
