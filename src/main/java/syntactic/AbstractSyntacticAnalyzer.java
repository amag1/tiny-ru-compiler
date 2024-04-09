package syntactic;

import exceptions.lexical.LexicalException;
import exceptions.syntactic.SyntacticException;
import lexical.Lexical;
import lexical.Token;
import lexical.Type;
import location.Location;

/**
 * Clase abstracta con layout para implementaciones concretas
 * de analizadores sintácticos.
 * <p>
 * Esta clase provee métodos para el análisis sintáctico a partir de un analizador lexico.
 */
public abstract class AbstractSyntacticAnalyzer {
    private final Lexical lexicalAnalyzer;
    private Token currentToken;

    public AbstractSyntacticAnalyzer(Lexical lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    /**
     * Llama al analizador sintáctico.
     *
     * @throws LexicalException si ocurre un error en el análisis léxico.
     */
    protected void nextToken() throws LexicalException {
        currentToken = lexicalAnalyzer.nextToken();
    }

    /**
     * Wrapper para obtener el tipo del token actual.
     *
     * @return el tipo del token actual, o EOF si no hay más tokens.
     */
    protected Type getTokenType() {
        if (currentToken == null) {
            currentToken = new Token("", Type.EOF, new Location(lexicalAnalyzer.getLine(), lexicalAnalyzer.getColumn()));
        }
        return currentToken.getType();
    }

    /**
     * Metodo para matchear el token actual con uno de los tipos esperados.
     * <p>
     * Si el token actual es de uno de los tipos esperados, se llama a nextToken()
     * para avanzar con el analisis sintactico.
     *
     * @param expected tipos de tokens esperados.
     * @throws LexicalException   si ocurre un error en el análisis léxico.
     * @throws SyntacticException si el token actual no es de uno de los tipos esperados.
     */
    protected void match(Type... expected) throws LexicalException, SyntacticException {
        for (Type type : expected) {
            if (getTokenType() == type) {
                nextToken();
                return;
            }
        }

        throw new SyntacticException(currentToken, expected);
    }

    protected void throwSyntacticException(Type... expected) throws SyntacticException {
        throw new SyntacticException(currentToken, expected);
    }
}