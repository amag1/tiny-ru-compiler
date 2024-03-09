package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import location.Location;
import reader.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LexicalTestTokenFailure {
    private String codeText;
    private LexicalException expectedException;

    public LexicalTestTokenFailure(String codeText, LexicalException expectedException) {
        this.codeText = codeText;
        this.expectedException = expectedException;
    }

    public void run() {
        Lexical lexical = new LexicalAnalyzer(new StringReader(codeText));
        while (!lexical.isEndOfFile()) {
            try {
                Token currentToken = lexical.nextToken();
            } catch (LexicalException e) {
                assertEquals(this.expectedException.getClass(), e.getClass(), "Different exception classes");
                assertEquals(this.expectedException.getColumn(), lexical.getColumn(), "Different exception columns");
                assertEquals(this.expectedException.getLine(), lexical.getLine(), "Different exception lines");
                return;
            }
        }

        // No se encontró la excepción
        fail("Exception Not Found");
    }
}
