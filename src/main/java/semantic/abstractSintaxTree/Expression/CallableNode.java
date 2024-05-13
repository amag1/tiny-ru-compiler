package semantic.abstractSintaxTree.Expression;

import java.util.ArrayList;
import java.util.List;

import exceptions.semantic.syntaxTree.AstException;
import exceptions.semantic.syntaxTree.ParameterCountMismatchException;
import exceptions.semantic.syntaxTree.ParameterTypeMismatchException;
import lexical.Token;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;
import semantic.symbolTable.SymbolTableLookup;
import semantic.symbolTable.VariableEntry;

public abstract class CallableNode extends PrimaryNode {
    private List<ExpressionNode> parameters;

    private String name;

    public CallableNode(Token token) {
        this.name = token.getLexem();
        this.token = token;
        this.parameters = new ArrayList<>();
    }

    public CallableNode(Token token, String name) {
        this.name = name;
        this.token = token;
        this.parameters = new ArrayList<>();
    }

    public void setParameters(List<ExpressionNode> parameters) {
        this.parameters = parameters;
    }

    protected void checkParametersMatch(Context context, List<VariableEntry> parameters) throws AstException {
        if (parameters.size() != this.parameters.size()) {
            throw new ParameterCountMismatchException(this.name, this.parameters.size(), parameters.size(), this.token);
        }

        for (int i = 0; i < parameters.size(); i++) {
            AttributeType hasType = this.parameters.get(i).getAttributeType(context);
            AttributeType expectedType = parameters.get(i).getType();

            if (!context.checkTypes(expectedType, hasType)) {
                throw new ParameterTypeMismatchException(expectedType.getType(), hasType.getType(), this.token);
            }
        }
    }

    protected List<ExpressionNode> getParameters() {
        return this.parameters;
    }
}
