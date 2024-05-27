package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Sentence.*;

import java.util.List;

/**
 * Interfaz para el handler del AST
 * <p>
 * Se utiliza para inyectarla dentro del analizador sintactico
 *
 * @see semantic.abstractSintaxTree.TinyRuAstHandler
 */
public interface AstHandler {
    /**
     * Verifica la validez de todas las sentencias
     *
     * @throws AstException Lanza una excepcion si alguna sentencia no es valida
     */
    void validateSenteces() throws AstException;

    LiteralNode createLiteral(Token token);

    VariableAccessNode createVariableAccess(Token token);

    ArrayAccessNode createArrayAccess(Token token, ExpressionNode indexExpression);

    PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode);

    MethodCallNode createMethodCallNode(Token methodToken);

    ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression);

    ConstructorCallNode createConstructorCallNode(Token classToken);

    NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression);

    StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken);

    SelfAccess createSelfAccess(PrimaryNode node, Token selfKeyword);

    ExpressionNode createUnaryExpressionNode(Token operator, ExpressionNode expression);

    BinaryOperationNode createBinaryOperationNode(Token operator, ExpressionNode left, ExpressionNode right);

    SimpleSentenceNode createSimpleSentenceNode(ExpressionNode expression);

    ReturnNode createEmptyReturnNode(Token token);

    ReturnNode createReturnNode(ExpressionNode exp, Token token);

    EmptySentenceNode createEmptySentenceNode();

    IfElseNode createIfElseNode(ExpressionNode condition, SentenceNode ifSentence, SentenceNode elseSentence);

    WhileNode createWhileNode(ExpressionNode condition, SentenceNode sentence);

    AssignationNode createAssignationNode(PrimaryNode leftSide, ExpressionNode rightNode);

    BlockNode createBlockNode(List<SentenceNode> sentences);

    void SetMethodParameter(CallableNode method, List<ExpressionNode> parameters);

    void addSentence(SentenceNode sentence);

    void createMethodIfNotExists(Token methodName);

    AbstractSyntaxTree getAst();

    public String toJson();
}
