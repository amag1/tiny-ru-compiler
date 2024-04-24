package semantic;

import java.util.Map;

public class JsonHelper {
    public static String json(String name, Map<String, ? extends Json> map, int identationIndex) {

        String identationStr = getIdentationString(identationIndex);

        String json = "\n" + identationStr + "\"" + name + "\": ";
        if (map == null || map.isEmpty()) {
            return json + "[]";
        }

        identationIndex++;
        identationStr = getIdentationString(identationIndex);

        json += "[";

        for (String key : map.keySet()) {
            json += "\n" + identationStr + map.get(key).toJson(identationIndex) + ",";
        }

        // Remove last comma
        json = json.substring(0, json.length() - 1);

        json += "\n" + JsonHelper.getIdentationString(identationIndex - 1) + "]";
        return json;
    }

    public static String json(String name, int value, int identationIndex) {
        return "\n" + getIdentationString(identationIndex) + "\"" + name + "\": " + value;
    }

    public static String json(String name, String value, int identationIndex) {
        return "\n" + getIdentationString(identationIndex) + "\"" + name + "\": " + value;
    }

    public static String json(String name, boolean value, int identationIndex) {
        return "\n" + getIdentationString(identationIndex) + "\"" + name + "\": " + value;
    }

    public static String getIdentationString(int identationIndex) {
        String identationString = "";
        for (int i = 1; i <= identationIndex; i++) {
            identationString += "\t";
        }
        return identationString;
    }

}
