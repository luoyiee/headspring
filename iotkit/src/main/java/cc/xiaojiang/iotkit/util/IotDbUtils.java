package cc.xiaojiang.iotkit.util;

import android.content.Context;

import cc.xiaojiang.iotkit.IotKit;

public class IotDbUtils {
    private static final String TAG = "IotDbUtils";
    private static ASimpleCache aSimpleCache;

    /**
     * key string
     */
    private static final String USER_SECRET = "user_secret";
    private static final String API_TOKEN = "api_token";
    private static final String USER_ID = "user_id";

    public static void init(Context context,String cacheName) {
        aSimpleCache = ASimpleCache.get(context, cacheName);
    }

    /**
     * 清除数据
     */
    public static void clear() {
        aSimpleCache.clear();
    }

    /**
     * 授权token
     */
    public static String getApiToken() {
        return aSimpleCache.getAsString(API_TOKEN);
    }

    public static void setApiToken(String accessToken) {
        LogUtil.d(TAG, "set API_TOKEN: " + accessToken);
        aSimpleCache.put(API_TOKEN, accessToken);
    }

//    /**
//     * mqtt登录使用,user_secret
//     */
//    public static String getUserSecret() {
//        return aSimpleCache.getAsString(USER_SECRET);
//    }
//
//    public static void setUserSecret(String userSecret) {
//        aSimpleCache.put(USER_SECRET, userSecret);
//        LogUtil.d(TAG, "set userSecret: " + userSecret);
//    }
//
//    /**
//     * 云平台userId
//     */
//    public static String getUserId() {
//        return aSimpleCache.getAsString(USER_ID);
//    }
//
//    public static void setUserId(String userId) {
//        aSimpleCache.put(USER_ID, userId);
//        LogUtil.d(TAG, "set userId: " + userId);
//    }
}
