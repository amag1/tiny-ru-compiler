package semantic.abstractSintaxTree.Expression;

import exceptions.semantic.syntaxTree.AstException;
import lexical.Token;
import semantic.abstractSintaxTree.AbstractSyntaxNode;
import semantic.abstractSintaxTree.Context;
import semantic.symbolTable.AttributeType;

/**
 * Clase que representa un nodo de una expresión en el AST
 */
public abstract class ExpressionNode extends AbstractSyntaxNode {

    protected AttributeType attributeType;

    protected Token token;

    /**
     * Todas las expresiones deben tener un tipo. Este método se encarga de
     * hallarlo y lanzar excepciones si no es posible
     * <p>
     * También se encarga de verificar que la expresión sea válida.
     * Esto puede verse en validaciones como asegurar que el índice de un array sea un entero
     *
     * @param context Contexto de ejecución
     * @return Tipo de atributo que representa la expresión
     * @throws AstException Lanza una excepción si la expresión no es válida
     */
    public abstract AttributeType getAttributeType(Context context) throws AstException;

    public Token getToken() {
        return token;
    }

    protected void setToken(Token token) {
        this.token = token;
    }
}
