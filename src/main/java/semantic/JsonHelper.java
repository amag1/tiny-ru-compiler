package semantic;

import java.util.Map;

public class JsonHelper {
    public static String json(String name, Map<String, ? extends Json> map) {
        String json = "\"" + name + "\": ";
        if (map == null || map.isEmpty()) {
            return  json + "[]";
        }

         json += "[";

        for (String key : map.keySet()) {
            json += "\n\t" + map.get(key).toJson() + ",";
        }

        // Remove last comma
        json = json.substring(0, json.length()-1);

        json += "]";
        return json;
    }

    public static String json(String name, int value) {
        return  "\"" + name + "\": " + value;
    }

    public static String json(String name, String value) {
        return  "\"" + name + "\": " + value;
    }

    public static String json(String name, boolean value) {
        return  "\"" + name + "\": " + value;
    }

}
