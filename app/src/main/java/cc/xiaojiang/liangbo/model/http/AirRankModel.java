package cc.xiaojiang.liangbo.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class AirRankModel {


    /**
     * country : CN
     * path : 中山,中山,广东,中国
     * city : 中山
     * timezoneOffset : +08:00
     * timezone : Asia/Shanghai
     * aqi : 10
     * rank : 1
     * time : 20180720
     */

    private String country;
    private String path;
    private String city;
    private String timezoneOffset;
    private String timezone;
    private int aqi;
    private int rank;
    private int time;

    public String getCountry() {
        return country == null ? "" : country;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public String getTimezoneOffset() {
        return timezoneOffset == null ? "" : timezoneOffset;
    }

    public String getTimezone() {
        return timezone == null ? "" : timezone;
    }

    public int getAqi() {
        return aqi;
    }

    public int getRank() {
        return rank;
    }

    public int getTime() {
        return time;
    }
}
