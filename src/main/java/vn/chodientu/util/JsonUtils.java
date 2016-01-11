package vn.chodientu.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author phugt
 */
public class JsonUtils {

    private static final Gson gson = new Gson();

    public static String encode(Object obj) {
        return gson.toJson(obj);
    }

    public static Object decode(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isJsonArray(String json) {
        try {
            List<String> listIds = gson.fromJson(json, new TypeToken<List<String>>() {
            }.getType());
            if (listIds != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
