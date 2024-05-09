package semantic;

import semantic.abstractSintaxTree.Sentence.SentenceNode;
import semantic.symbolTable.MethodEntry;

import java.util.List;
import java.util.Map;

/**
 * Clase que provee metodos estaticos para la generacion de JSON
 * Esta clase no tiene atributos
 */
public class JsonHelper {
    /**
     * @param name             Nombre del atributo
     * @param map              Mapa de objetos que implementan la interfaz Json
     * @param indentationIndex Indentacion del JSON
     * @return Un string con el JSON del mapa
     */
    public static String json(String name, Map<String, ? extends Json> map, int indentationIndex) {

        String identationStr = getIdentationString(indentationIndex);

        String json = "\n" + identationStr + "\"" + name + "\": ";
        if (map == null || map.isEmpty()) {
            return json + "[]";
        }

        indentationIndex++;
        identationStr = getIdentationString(indentationIndex);

        json += "[";

        for (String key : map.keySet()) {
            json += "\n" + identationStr + map.get(key).toJson(indentationIndex) + ",";
        }

        // Remove last comma
        json = json.substring(0, json.length() - 1);

        json += "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "]";
        return json;
    }

    /**
     * Metodo para generar el JSON de un entero
     *
     * @param name             El nombre del atributo
     * @param value            El valor de tipo int del atributo
     * @param indentationIndex El nivel de indentacion
     * @return Un String respetando el formato JSON
     */
    public static String json(String name, int value, int indentationIndex) {
        return "\n" + getIdentationString(indentationIndex) + "\"" + name + "\": " + value;
    }

    /**
     * @param name             Nombre del atributo
     * @param value            Valor del atributo tipo String
     * @param indentationIndex Indentacion del JSON
     * @return Un string con el JSON del atributo
     */
    public static String json(String name, String value, int indentationIndex) {
        return "\n" + getIdentationString(indentationIndex) + "\"" + name + "\": " + "\"" + value + "\"";
    }

    /**
     * @param name             Nombre del atributo
     * @param value            Valor del atributo tipo boolean
     * @param indentationIndex Indentacion del JSON
     * @return Un string con el JSON del atributo
     */
    public static String json(String name, boolean value, int indentationIndex) {
        return "\n" + getIdentationString(indentationIndex) + "\"" + name + "\": " + value;
    }


    public static String json(String name, List< ? extends Json> list, int indentationIndex) {
        String identationStr = getIdentationString(indentationIndex);

        String json = "\n" + identationStr + "\"" + name + "\": ";
        if (list == null || list.isEmpty()) {
            return json + "[]";
        }

        indentationIndex++;
        identationStr = getIdentationString(indentationIndex);

        json += "[";

        for (Json element : list) {
            json += "\n" + identationStr + element.toJson(indentationIndex) + ",";
        }

        // Remove last comma
        json = json.substring(0, json.length() - 1);

        json += "\n" + JsonHelper.getIdentationString(indentationIndex - 1) + "]";
        return json;
    }

    public static String json(String name,  Json element, int indentationIndex) {
        String identationStr = getIdentationString(indentationIndex);
        String json = "\n" + identationStr + "\"" + name + "\": ";
        if (element == null) {
            return json + "null";
        }
        return json + element.toJson(indentationIndex);
    }

    /**
     * @param identationIndex Indentacion del JSON
     * @return Un string con la cantidad de tabulaciones necesarias
     */
    public static String getIdentationString(int identationIndex) {
        String identationString = "";
        for (int i = 1; i <= identationIndex; i++) {
            identationString += "\t";
        }
        return identationString;
    }

}
