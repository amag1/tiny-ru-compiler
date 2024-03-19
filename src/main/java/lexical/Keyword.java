package lexical;

import lexical.Type;

import java.util.HashMap;

/**
 * Esta clase se encarga de manejar las palabras reservadas del lenguaje.
 * Permite recuperar el tipo asociado a una palabra reservada
 */
public class Keyword {
    /**
     * Mapa que asocia una palabra reservada con su tipo
     */
    private static final HashMap<String, Type> keywords = new HashMap<String, Type>();

    static {
        keywords.put("struct", Type.KW_STRUCT);
        keywords.put("impl", Type.KW_IMPL);
        keywords.put("else", Type.KW_ELSE);
        keywords.put("false", Type.KW_FALSE);
        keywords.put("if", Type.KW_IF);
        keywords.put("ret", Type.KW_RET);
        keywords.put("while", Type.KW_WHILE);
        keywords.put("true", Type.KW_TRUE);
        keywords.put("nil", Type.KW_NIL);
        keywords.put("new", Type.KW_NEW);
        keywords.put("fn", Type.KW_FN);
        keywords.put("st", Type.KW_ST);
        keywords.put("pri", Type.KW_PRI);
        keywords.put("self", Type.KW_SELF);
        keywords.put("void", Type.TYPE_VOID);
        keywords.put("start", Type.KW_START);
    }

    /**
     * @param keyword una palabra reservada
     * @return el tipo asociado a la palabra reservada
     */
    public static Type getKeywordType(String keyword) {
        return keywords.get(keyword);
    }
}
