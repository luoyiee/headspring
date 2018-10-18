package cc.xiaojiang.liangbo.model.http;

import java.util.List;

public class HomeNewWeatherModel {


    /**
     * countyWeather : {"ymdh":2018101109,"feelsLike":18,"countyId":"WTQ3TQRKQ701",
     * "lastUpdate":1539221100000,"temperature":18,"humidity":59,"text":"多云","windDirection":"北",
     * "windSpeed":"23.76","countyName":"鄞州","windScale":"4"}
     * airQuality : {"ymdh":2018101109,"no2":39,"pm25":8,"o3":40,"countyId":"WTQ3TQRKQ701",
     * "so2":8,"lastUpdate":1539216000000,"aqi":20,"pm10":17,"co":0.61}
     * countyForecast : [{"ymdh":2018101209,"code":4,"countyId":"WTQ3TQRKQ701",
     * "highTemperature":21,"lastUpdate":1539216000000,"text":"多云","lowTemperature":13,
     * "windDirection":"东北","windSpeed":"15","day":"2018-10-12","windScale":3},
     * {"ymdh":2018101309,"code":4,"countyId":"WTQ3TQRKQ701","highTemperature":23,
     * "lastUpdate":1539216000000,"text":"多云","lowTemperature":16,"windDirection":"东",
     * "windSpeed":"10","day":"2018-10-13","windScale":2},{"ymdh":2018101409,"code":4,
     * "countyId":"WTQ3TQRKQ701","highTemperature":24,"lastUpdate":1539216000000,"text":"多云",
     * "lowTemperature":16,"windDirection":"东","windSpeed":"10","day":"2018-10-14",
     * "windScale":2},{"ymdh":2018101509,"code":13,"countyId":"WTQ3TQRKQ701",
     * "highTemperature":22,"lastUpdate":1539216000000,"text":"小雨","lowTemperature":17,
     * "windDirection":"东北","windSpeed":"10","day":"2018-10-15","windScale":2}]
     */

    private CountyWeatherBean countyWeather;
    private AirQualityBean airQuality;
    private List<CountyForecastBean> countyForecast;

    public CountyWeatherBean getCountyWeather() {
        return countyWeather;
    }

    public void setCountyWeather(CountyWeatherBean countyWeather) {
        this.countyWeather = countyWeather;
    }

    public AirQualityBean getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQualityBean airQuality) {
        this.airQuality = airQuality;
    }

    public List<CountyForecastBean> getCountyForecast() {
        return countyForecast;
    }

    public void setCountyForecast(List<CountyForecastBean> countyForecast) {
        this.countyForecast = countyForecast;
    }

    public static class CountyWeatherBean {
        /**
         * ymdh : 2018101109
         * feelsLike : 18
         * countyId : WTQ3TQRKQ701
         * lastUpdate : 1539221100000
         * temperature : 18
         * humidity : 59
         * text : 多云
         * windDirection : 北
         * windSpeed : 23.76
         * countyName : 鄞州
         * windScale : 4
         */

        private int ymdh;
        private int feelsLike;
        private String countyId;
        private long lastUpdate;
        private int temperature;
        private int humidity;
        private String text;
        private String windDirection;
        private String windSpeed;
        private String countyName;
        private String windScale;

        public int getYmdh() {
            return ymdh;
        }

        public void setYmdh(int ymdh) {
            this.ymdh = ymdh;
        }

        public int getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(int feelsLike) {
            this.feelsLike = feelsLike;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }

        public String getWindScale() {
            return windScale;
        }

        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }
    }

    public static class AirQualityBean {
        /**
         * ymdh : 2018101109
         * no2 : 39
         * pm25 : 8
         * o3 : 40
         * countyId : WTQ3TQRKQ701
         * so2 : 8
         * lastUpdate : 1539216000000
         * aqi : 20
         * pm10 : 17
         * co : 0.61
         */

        private int ymdh;
        private int no2;
        private int pm25;
        private int o3;
        private String countyId;
        private int so2;
        private long lastUpdate;
        private int aqi;
        private int pm10;
        private double co;

        public int getYmdh() {
            return ymdh;
        }

        public void setYmdh(int ymdh) {
            this.ymdh = ymdh;
        }

        public int getNo2() {
            return no2;
        }

        public void setNo2(int no2) {
            this.no2 = no2;
        }

        public int getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public int getO3() {
            return o3;
        }

        public void setO3(int o3) {
            this.o3 = o3;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }

        public int getSo2() {
            return so2;
        }

        public void setSo2(int so2) {
            this.so2 = so2;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public int getPm10() {
            return pm10;
        }

        public void setPm10(int pm10) {
            this.pm10 = pm10;
        }

        public double getCo() {
            return co;
        }

        public void setCo(double co) {
            this.co = co;
        }
    }

    public static class CountyForecastBean {
        /**
         * ymdh : 2018101209
         * code : 4
         * countyId : WTQ3TQRKQ701
         * highTemperature : 21
         * lastUpdate : 1539216000000
         * text : 多云
         * lowTemperature : 13
         * windDirection : 东北
         * windSpeed : 15
         * day : 2018-10-12
         * windScale : 3
         */

        private int ymdh;
        private int code;
        private String countyId;
        private int highTemperature;
        private long lastUpdate;
        private String text;
        private int lowTemperature;
        private String windDirection;
        private String windSpeed;
        private String day;
        private int windScale;

        public int getYmdh() {
            return ymdh;
        }

        public void setYmdh(int ymdh) {
            this.ymdh = ymdh;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }

        public int getHighTemperature() {
            return highTemperature;
        }

        public void setHighTemperature(int highTemperature) {
            this.highTemperature = highTemperature;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getLowTemperature() {
            return lowTemperature;
        }

        public void setLowTemperature(int lowTemperature) {
            this.lowTemperature = lowTemperature;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getWindScale() {
            return windScale;
        }

        public void setWindScale(int windScale) {
            this.windScale = windScale;
        }
    }
}
