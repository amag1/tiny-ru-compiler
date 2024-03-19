package lexical;

import java.util.HashMap;

/**
 * Esta clase se encarga de manejar los tipos primitivos del lenguaje.
 */
public class PrimitiveType {
    /**
     * Mapa que asocia un tipo primitivo con su tipo
     */
    private static final HashMap<String, Type> types = new HashMap<String, Type>();

    static {
        types.put("Int", Type.TYPE_INT);
        types.put("Char", Type.TYPE_CHAR);
        types.put("String", Type.TYPE_STRING);
        types.put("Bool", Type.TYPE_BOOL);
        types.put("Array", Type.ARRAY);
    }

    /**
     * @param type un tipo primitivo
     * @return el tipo asociado al tipo primitivo
     */
    public static Type getType(String type) {
        return types.get(type);
    }

}
