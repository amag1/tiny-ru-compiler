package semantic.abstractSintaxTree;

import exceptions.semantic.symbolTable.SymbolTableException;
import lexical.Token;
import semantic.abstractSintaxTree.Expression.*;
import semantic.abstractSintaxTree.Expression.ConstructorCallNode;
import semantic.abstractSintaxTree.Sentence.*;

import java.util.List;

public interface AstHandler {
    void validateSenteces() throws SymbolTableException;

    LiteralNode createLiteral(Token token);

    VariableAccessNode createVariableAccess(Token token);

    ArrayAccessNode createArrayAccess(Token token, ExpressionNode indexExpression);

    PrimaryNode handlePossibleChain(PrimaryNode parentNode, PrimaryNode childrenNode);

    MethodCallNode createMethodCallNode(Token methodToken);

    ParentizedExpressionNode createParentizedExpressionNode(ExpressionNode expression);

    ConstructorCallNode createConstructorCallNode(Token classToken);

    NewArrayNode createNewArrayNode(Token elementsTypeToken, ExpressionNode lengthExpression);

    StaticMethodCallNode createStaticMethodCallNode(Token classToken, Token methodToken);

    SelfAccess createSelfAccess(PrimaryNode node);

    ExpressionNode createUnaryExpressionNode(Token operator, ExpressionNode expression);

    BinaryOperationNode createBinaryOperationNode(Token operator, ExpressionNode left, ExpressionNode right);

    SimpleSentenceNode createSimpleSentenceNode(ExpressionNode expression);

    ReturnNode createEmptyReturnNode();

    ReturnNode createReturnNode(ExpressionNode exp);

    EmptySentenceNode createEmptySentenceNode();

    IfElseNode createIfElseNode(ExpressionNode condition, SentenceNode ifSentence, SentenceNode elseSentence);

    WhileNode createWhileNode(ExpressionNode condition, SentenceNode sentence);

    AssignationNode createAssignationNode(PrimaryNode leftSide, ExpressionNode rightNode);

    BlockNode createBlockNode(List<SentenceNode> sentences);

    void SetMethodParameter(MethodCall method, List<ExpressionNode> parameters);

    void addSentence(SentenceNode sentence);

    public String toJson();
}
