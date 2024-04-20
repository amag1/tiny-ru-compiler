package semantic;

import java.util.Map;

public class JsonHelper {
    public static String json(Map<String, ? extends Json> map) {
        StringBuilder json = new StringBuilder("[\n");
        for (String key : map.keySet()) {
            json.append("\t").append(map.get(key).toJson()).append(",\n");
        }

        if (json.length() > 2) {
            json.deleteCharAt(json.length() - 2);
        }
        json.append("]");
        return json.toString();
    }
}
