package cc.xiaojiang.iotkit.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JsonUtils {
    public String map2JsonStr(HashMap<String, Object> hashMap) {
        String jsonStr = "";
        try {
            JSONObject jsonObject = new JSONObject();
            for (String key : hashMap.keySet()) {
                Object value = hashMap.get(key);
                jsonObject.put(key, value);
            }
            jsonStr = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }
}
