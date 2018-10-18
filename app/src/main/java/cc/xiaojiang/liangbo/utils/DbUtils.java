package cc.xiaojiang.liangbo.utils;


import cc.xiaojiang.liangbo.base.MyApplication;
import cc.xiaojiang.iotkit.util.ASimpleCache;

public class DbUtils {
    private static final String CACHE_NAME = "db_headspring";
    private static ASimpleCache aSimpleCache = ASimpleCache.get(MyApplication.getInstance(),
            CACHE_NAME);

    public static void clear() {
        aSimpleCache.clear();
    }

    /**
     * key string
     */
    private static final String USER_ID = "user_id";
    private static final String XJ_USER_ID = "xj_user_id";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String ACCOUNT_PHONE = "account_phone";
    private static final String LOCATION_CITY = "location_city";


    public static void setWifiPwd(String ssid, String pwd) {
        aSimpleCache.put(ssid, pwd);
    }

    public static String getPwdBySsid(String ssid) {
       return aSimpleCache.getAsString(ssid);
    }

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
     * 保存用户手机号
     *
     * @param phoneNumber 手机号
     */
    public static void setAccountPhoneNumber(String phoneNumber) {
        aSimpleCache.put(ACCOUNT_PHONE, phoneNumber);
    }

    /**
     * 获取用户手机号
     */
    public static String getAccountPhoneNumber() {
        return aSimpleCache.getAsString(ACCOUNT_PHONE);
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


    /**
     * 用户定位城市
     */
    public static void setLocationCity(String city) {
        aSimpleCache.put(LOCATION_CITY, city);

    }

    public static String getLocationCity() {
        return aSimpleCache.getAsString(LOCATION_CITY);
    }
}
