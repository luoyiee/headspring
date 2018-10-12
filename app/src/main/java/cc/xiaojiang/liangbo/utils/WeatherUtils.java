package cc.xiaojiang.liangbo.utils;

import cc.xiaojiang.liangbo.model.realm.WeatherCityCodeRealm;
import io.realm.Realm;

public class WeatherUtils {
    public static String getXinZhiCityCode(String cityCode, String adCode) {
        Realm realm = Realm.getDefaultInstance();
        WeatherCityCodeRealm weatherCityCodeRealm = realm.where(WeatherCityCodeRealm.class)
                .equalTo("citycode", cityCode)
                .equalTo("adcode", adCode)
                .findFirst();
        String code = weatherCityCodeRealm == null ? null : weatherCityCodeRealm.getXz_citycode();
        realm.close();
        return code;
    }

    public static String getXinZhiCityCode(String city) {
        Realm realm = Realm.getDefaultInstance();
        WeatherCityCodeRealm weatherCityCodeRealm = realm.where(WeatherCityCodeRealm.class)
                .equalTo("name", city)
                .findFirst();
        String code = weatherCityCodeRealm == null ? null : weatherCityCodeRealm.getXz_citycode();
        realm.close();
        return code;
    }

    public static String getAirQuality(int aqi) {
        String rank = "";
        if (aqi <= 0) {
            rank = "--";
        } else if (aqi <= 50) {
            rank = "优";
        } else if (aqi <= 100) {
            rank = "良";
        } else if (aqi <= 150) {
            rank = "轻度污染";
        } else if (aqi <= 200) {
            rank = "中度污染";
        } else if (aqi <= 300) {
            rank = "重度污染";
        } else {
            rank = "严重污染";
        }
        return rank;
    }
}
