package lexical;

import exceptions.lexical.LexicalException;
import exceptions.lexical.MalformedClassIdentifierException;
import location.Location;
import reader.StringReader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LexicalTestTokenFailure {
    private String codeText;
    private LexicalException expectedException;

    public LexicalTestTokenFailure(String codeText, LexicalException expectedException) {
        this.codeText = codeText;
        this.expectedException = expectedException;
    }

    public void run(boolean locationChecking) {
        Lexical lexical = new LexicalAnalyzer(new StringReader(codeText));

        List<Token> tokens = new ArrayList<Token>();
        try {
            Token token = lexical.nextToken();

            while (token != null) {
                tokens.add(token);
                token = lexical.nextToken();
            }
        } catch (LexicalException e) {
            assertEquals(this.expectedException.getClass(), e.getClass(), "Different exception classes");

            if (locationChecking) {
                assertEquals(this.expectedException.getColumn(), lexical.getColumn(), "Different exception columns");
                assertEquals(this.expectedException.getLine(), lexical.getLine(), "Different exception lines");
            }

            return;
        }

        // No se encontró la excepción
        fail("Exception Not Found");
    }

    public void run(){
        this.run(true);
    }
}
