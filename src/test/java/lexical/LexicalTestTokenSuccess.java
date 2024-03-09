package lexical;

import exceptions.lexical.LexicalException;
import reader.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LexicalTestTokenSuccess {
    private final Token[] expectedTokens;
    private final String codeText;
    public LexicalTestTokenSuccess(String codeText, Token[] expectedTokens) {
        this.codeText = codeText;
        this.expectedTokens = expectedTokens;
    }

    public void run() {
        Lexical lexical = new LexicalAnalyzer(new StringReader(codeText));
        Token currentToken;
        for (Token expectedToken: expectedTokens) {
            try {
                currentToken = lexical.nextToken();
                assertEquals(expectedToken.getLexem(),currentToken.getLexem(), "Different token lexems") ;
                assertEquals(expectedToken.getType(),currentToken.getType(), "Different token types");
                assertEquals(expectedToken.getLine(),currentToken.getLine(), "Different token lines");
                assertEquals(expectedToken.getColumn(),currentToken.getColumn(), "Different token columns");
            } catch (LexicalException e) {
                fail();
            }
        }
    }
}
