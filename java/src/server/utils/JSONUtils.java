package server.utils;

import com.google.gson.Gson;

/**
 * @author Derek Argueta
 */
public final class JSONUtils {
    private static final Gson gson = new Gson();

    private JSONUtils(){}

    public static boolean isJSONValid(String JSON_STRING) {
        if(JSON_STRING.length() < 2) return false;
        try {
            gson.fromJson(JSON_STRING, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
}
