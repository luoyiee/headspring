package cc.xiaojiang.headspring.model.http;

public class AqiModel {
    /**
     * o3 : 89
     * level : 1
     * city : 呼伦贝尔
     * timezone : Asia/Shanghai
     * latitude : 49.212189
     * timezone_offset : +08:00
     * pm10 : 17
     * longtitude : 119.736279
     * co : 0.3
     * quality : 优
     * no2 : 11
     * pm25 : 8
     * province : 内蒙古
     * so2 : 2
     * last_update : 1531213200000
     * aqi : 28
     * station : null
     * primary_pollutant : null
     * time : 2018071018
     */

    private int o3;
    private int level;
    private String city;
    private String timezone;
    private double latitude;
    private String timezone_offset;
    private int pm10;
    private double longtitude;
    private double co;
    private String quality;
    private int no2;
    private int pm25;
    private String province;
    private int so2;
    private long last_update;
    private int aqi;
    private Object station;
    private Object primary_pollutant;
    private int time;

    public int getO3() {
        return o3;
    }

    public int getLevel() {
        return level;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public String getTimezone() {
        return timezone == null ? "" : timezone;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getTimezone_offset() {
        return timezone_offset == null ? "" : timezone_offset;
    }

    public int getPm10() {
        return pm10;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getCo() {
        return co;
    }

    public String getQuality() {
        return quality == null ? "" : quality;
    }

    public int getNo2() {
        return no2;
    }

    public int getPm25() {
        return pm25;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public int getSo2() {
        return so2;
    }

    public long getLast_update() {
        return last_update;
    }

    public int getAqi() {
        return aqi;
    }

    public Object getStation() {
        return station;
    }

    public Object getPrimary_pollutant() {
        return primary_pollutant;
    }

    public int getTime() {
        return time;
    }
}
