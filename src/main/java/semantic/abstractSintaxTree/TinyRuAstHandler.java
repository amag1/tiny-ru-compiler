package semantic.abstractSintaxTree;

import exceptions.semantic.SemanticException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Sentence.*;
import semantic.symbolTable.ClassEntry;
import semantic.symbolTable.MethodEntry;
import semantic.symbolTable.SymbolTable;
import semantic.abstractSintaxTree.Expression.ConstructorCallNode;
import semantic.symbolTable.SymbolTableHandler;

import java.util.List;
import java.util.Map;

public class TinyRuAstHandler implements AstHandler {

    private AbstractSyntaxTree ast;
    private SymbolTableHandler stHandler;

    public TinyRuAstHandler(SymbolTableHandler stHandler) {
        this.ast = new AbstractSyntaxTree();
        this.stHandler = stHandler;
    }

    public void validateSenteces() throws SemanticException {
        for (Map.Entry<String,AstClassEntry> entry : ast.getClasses().entrySet()) {
            AstClassEntry currentClass = entry.getValue();
            currentClass.validateSentences();
        }
    }

    public LiteralNode createLiteral(Token token) {
        return new LiteralNode(token);
    }

    public VariableAccessNode createVariableAccess(Token token) {
        return new VariableAccessNode(token.getLexem());
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
        return new ConstructorCallNode(classToken.getLexem());
    }

    public NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        return new NewArrayNode(elementsTypeToken, lengthExpression);
    }

    public StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken) {
        return new StaticMethodCallNode(classToken.getLexem(), methodToken.getLexem());
    }

    public SelfAccess createSelfAccess(PrimaryNode node) {
        return new SelfAccess(node);
    }

    public UnaryOperationNode createUnaryExpressionNode(Token operator, ExpressionNode expression) {
        // TODO: verificar que matchee el tipo del operador y la expresion
        return new UnaryOperationNode(expression, operator);
    }

    public BinaryOperationNode createBinaryOperationNode(Token operator, ExpressionNode left, ExpressionNode right) {
        return new BinaryOperationNode(operator, left, right);
    }

    public void SetMethodParameter(MethodCall method, List<ExpressionNode> parameters) {
        method.setParameters(parameters);
    }

    public SimpleSentenceNode createSimpleSentenceNode(ExpressionNode expression) {
        return new SimpleSentenceNode(expression);
    }

    public ReturnNode createEmptyReturnNode() {
        return new ReturnNode();
    }

    public ReturnNode createReturnNode(ExpressionNode exp) {
        return new ReturnNode(exp);
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

    public void addSentence(SentenceNode sentence) {
        // Obtener la clase actual de la tabla de símbolos
        ClassEntry currentClass = stHandler.getCurrentClass();
        if (currentClass != null) {
            // Obtener o crear la clase en el ast
            AstClassEntry currentAstClass;
            currentAstClass = ast.getClass(currentClass.getName());
            if (currentAstClass == null) {
                currentAstClass = new AstClassEntry(currentClass.getName());
                ast.addClass(currentAstClass);
            }

            // Obtener o crear un nuevo método
            MethodEntry currentMethod = stHandler.getCurrentMethod();
            AstMethodEntry currentAstMethod = currentAstClass.getMethod(currentMethod.getName());
            if (currentAstMethod == null) {
                currentAstMethod = new AstMethodEntry(currentMethod.getName());
                currentAstClass.addMethod(currentAstMethod);
            }

            currentAstMethod.addSentence(sentence);
        }
        else {
            // Agregar al método start
            AstMethodEntry startMethod = ast.getStart();
            startMethod.addSentence(sentence);
        }
    }
}
