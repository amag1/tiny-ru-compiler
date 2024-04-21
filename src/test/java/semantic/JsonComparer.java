package semantic;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonComparer {
    public static boolean compare(String json1, String json2) {
        ObjectMapper map = new ObjectMapper();

        try {
            Object obj1 = map.readTree(json1);
            Object obj2 = map.readTree(json2);
            return obj1.equals(obj2);
        } catch (Exception e) {
            return false;
        }

    }
}
