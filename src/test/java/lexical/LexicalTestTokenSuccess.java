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

    public void run(boolean lexemChecking, boolean locationChecking) {
        Lexical lexical = new LexicalAnalyzer(new StringReader(codeText));
        Token currentToken;
        for (Token expectedToken : expectedTokens) {
            try {
                currentToken = lexical.nextToken();

                assertEquals(expectedToken.getType(), currentToken.getType(), "Different token types");

                if (lexemChecking) {
                    assertEquals(expectedToken.getLexem(), currentToken.getLexem(), "Different token lexems");
                }

                if (locationChecking) {
                    assertEquals(expectedToken.getLine(), currentToken.getLine(), "Different token lines");
                    assertEquals(expectedToken.getColumn(), currentToken.getColumn(), "Different token columns");
                }
            } catch (LexicalException e) {
                fail(e.getMessage());
            }
        }

        // Ensure that there are not more tokens
        try {
            Token nextToken = lexical.nextToken();
            if (nextToken != null) {
                fail("There are more tokens than expected");
            }
        } catch (LexicalException e){
        fail(e.getMessage());
        }
    }

    public void run(){
        this.run(true, true);
    }
}
