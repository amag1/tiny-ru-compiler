package semantic.abstractSintaxTree;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Sentence.*;

import java.util.List;

/**
 * Implementación vacía de AstHandler para poder realizar pruebas de etapas anteriores
 */
public class DummyAstHandler implements AstHandler {

    @Override
    public void validateSenteces() throws AstException {

    }

    @Override
    public LiteralNode createLiteral(Token token) {
        return null;
    }

    @Override
    public VariableAccessNode createVariableAccess(Token token) {
        return null;
    }

    @Override
    public ArrayAccessNode createArrayAccess(Token token, ExpressionNode indexExpression) {
        return null;
    }

    @Override
    public PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode) {
        return null;
    }

    @Override
    public MethodCallNode createMethodCallNode(Token methodToken) {
        return null;
    }

    @Override
    public ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression) {
        return null;
    }

    @Override
    public ConstructorCallNode createConstructorCallNode(Token classToken) {
        return null;
    }

    @Override
    public NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression) {
        return null;
    }

    @Override
    public StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken) {
        return null;
    }

    @Override
    public SelfAccess createSelfAccess(PrimaryNode node, Token selfKeyword) {
        return null;
    }

    @Override
    public ExpressionNode createUnaryExpressionNode(Token operator, ExpressionNode expression) {
        return null;
    }

    @Override
    public BinaryOperationNode createBinaryOperationNode(Token operator, ExpressionNode left, ExpressionNode right) {
        return null;
    }

    @Override
    public SimpleSentenceNode createSimpleSentenceNode(ExpressionNode expression) {
        return null;
    }

    @Override
    public ReturnNode createEmptyReturnNode(Token token) {
        return null;
    }

    @Override
    public ReturnNode createReturnNode(ExpressionNode exp, Token token) {
        return null;
    }

    @Override
    public EmptySentenceNode createEmptySentenceNode() {
        return null;
    }

    @Override
    public IfElseNode createIfElseNode(ExpressionNode condition, SentenceNode ifSentence, SentenceNode elseSentence) {
        return null;
    }

    @Override
    public WhileNode createWhileNode(ExpressionNode condition, SentenceNode sentence) {
        return null;
    }

    @Override
    public AssignationNode createAssignationNode(PrimaryNode leftSide, ExpressionNode rightNode) {
        return null;
    }

    @Override
    public BlockNode createBlockNode(List<SentenceNode> sentences) {
        return null;
    }

    @Override
    public void SetMethodParameter(CallableNode method, List<ExpressionNode> parameters) {

    }

    @Override
    public void addSentence(SentenceNode sentence) {

    }

    @Override
    public void createMethodIfNotExists(Token methodName) {

    }

    @Override
    public String toJson() {
        return null;
    }
}
