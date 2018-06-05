package cc.xiaojiang.headerspring.utils;


import cc.xiaojiang.baselibrary.util.ASimpleCache;
import cc.xiaojiang.headerspring.base.MyApplication;

public class DbUtils {
    private static final String CACHE_NAME = "db_headspring";
    private static ASimpleCache aSimpleCache = ASimpleCache.get(MyApplication.getInstance(),
            CACHE_NAME);

    /**
     * key string
     */
    private static final String USER_ID = "user_id";
    private static final String ACCESS_TOKEN = "access_token";

    /**
     * 用户ID
     */
    public static String getUserId() {
        return aSimpleCache.getAsString(USER_ID);
    }

    public static void setUserId(String userId) {
        aSimpleCache.put(USER_ID, userId);
    }

    /**
     * 授权token
     */
    public static String getAccessToken() {
        return aSimpleCache.getAsString(ACCESS_TOKEN);
    }

    public static void setAccessToken(String accessToken) {
        aSimpleCache.put(ACCESS_TOKEN, accessToken);
    }
}
