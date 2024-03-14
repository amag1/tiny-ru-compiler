package lexical;

import java.util.HashMap;

public class PrimitiveType {
    private static final HashMap<String, Type> types = new HashMap<String, Type>();

    static {
        types.put("Int", Type.TYPE_INT);
        types.put("Char", Type.TYPE_CHAR);
        types.put("String", Type.TYPE_STRING);
        types.put("Bool", Type.TYPE_BOOL);
        types.put("Array", Type.ARRAY);
    }

    public static Type getType(String type) {
        return types.get(type);
    }

}
