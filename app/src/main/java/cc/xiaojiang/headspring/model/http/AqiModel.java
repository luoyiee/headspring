package cc.xiaojiang.headspring.model.http;

public class AqiModel {


    /**
     * level : 0
     * aqi : 33
     * pm25 : 33
     * pm10 : 32
     * so2 : 15
     * no2 : 24
     * co : 0.6
     * o3 : 105
     * qunlity : null
     * longitude : 121.836
     * latitude : 29.9108
     * station : 区环保大楼
     * city : null
     * province : null
     * primaryPollutant : null
     * quality : null
     * time : null
     * lastUpdate : null
     * timezone : null
     * timezoneOffset : null
     */

    private int level;
    private int aqi;
    private int pm25;
    private int pm10;
    private int so2;
    private int no2;
    private double co;
    private int o3;
    private Object qunlity;
    private double longitude;
    private double latitude;
    private String station;
    private Object city;
    private Object province;
    private Object primaryPollutant;
    private Object quality;
    private Object time;
    private Object lastUpdate;
    private Object timezone;
    private Object timezoneOffset;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public int getSo2() {
        return so2;
    }

    public void setSo2(int so2) {
        this.so2 = so2;
    }

    public int getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
    }

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public int getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public Object getQunlity() {
        return qunlity;
    }

    public void setQunlity(Object qunlity) {
        this.qunlity = qunlity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }

    public Object getPrimaryPollutant() {
        return primaryPollutant;
    }

    public void setPrimaryPollutant(Object primaryPollutant) {
        this.primaryPollutant = primaryPollutant;
    }

    public Object getQuality() {
        return quality;
    }

    public void setQuality(Object quality) {
        this.quality = quality;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public Object getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Object lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Object getTimezone() {
        return timezone;
    }

    public void setTimezone(Object timezone) {
        this.timezone = timezone;
    }

    public Object getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Object timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
}
