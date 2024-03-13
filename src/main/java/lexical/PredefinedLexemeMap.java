package lexical;

import java.util.HashMap;

public class PredefinedLexemeMap {
    private static final HashMap<String, Type> types = new HashMap<String, Type>();

    static {
        types.put("==", Type.EQUAL);
        types.put("=", Type.ASSIGN);
        types.put(">", Type.GREATER);
        types.put(">=", Type.GREATER_EQUAL);
        types.put("<", Type.LESS);
        types.put("<=", Type.LESS_EQUAL);
        types.put("!=", Type.NOT_EQUAL);
        types.put("!", Type.NEG);
        types.put("+", Type.PLUS);
        types.put("-", Type.MINUS);
        types.put("++", Type.DPLUS);
        types.put("--", Type.DMINUS);
    }

    public static Type getType(String type) {
        return types.get(type);
    }

}
