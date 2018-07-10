package cc.xiaojiang.headspring.utils;


import cc.xiaojiang.baselibrary.util.ASimpleCache;
import cc.xiaojiang.headspring.base.MyApplication;

public class DbUtils {
    private static final String CACHE_NAME = "db_headspring";
    private static ASimpleCache aSimpleCache = ASimpleCache.get(MyApplication.getInstance(),
            CACHE_NAME);

    /**
     * key string
     */
    private static final String USER_ID = "user_id";
    private static final String XJ_USER_ID = "xj_user_id";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

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
     * access_token
     */
    public static String getAccessToken() {
        return aSimpleCache.getAsString(ACCESS_TOKEN);
    }

    public static void setAccessToken(String accessToken) {
        aSimpleCache.put(ACCESS_TOKEN, accessToken);
    }

    /**
     * refresh_token
     */
    public static String getRefreshToken() {
        return aSimpleCache.getAsString(REFRESH_TOKEN);
    }

    public static void setRefreshToken(String refreshToken) {
        aSimpleCache.put(REFRESH_TOKEN, refreshToken);
    }

    /**
     * 获取小匠云用户id
     */
    public static String getXJUserId() {
        return aSimpleCache.getAsString(XJ_USER_ID);
    }

    public static void setXJUserId(String xjUserId) {
        aSimpleCache.put(XJ_USER_ID, xjUserId);
    }

    public static void clear(){
        aSimpleCache.clear();
    }

}
