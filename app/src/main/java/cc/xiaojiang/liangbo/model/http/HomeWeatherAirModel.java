package cc.xiaojiang.liangbo.model.http;

import java.util.List;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class HomeWeatherAirModel {

    /**
     * pm25 : 7
     * temperature : 28
     * humidity : 75
     * nextWeather : [{"addTime":1533260388126,"city":"宁波市","codeDay":4,
     * "windDirectionDegree":180,"textDay":"多云","precip":0,"codeNight":4,"lowTemp":27,
     * "lastUpdate":1533168000000,"textNight":"多云","highTemp":35,"windDirection":"南",
     * "windSpeed":15,"day":20180804,"windScale":3},{"addTime":1533260388126,"city":"宁波市",
     * "codeDay":4,"windDirectionDegree":180,"textDay":"多云","precip":0,"codeNight":4,
     * "lowTemp":27,"lastUpdate":1533168000000,"textNight":"多云","highTemp":35,
     * "windDirection":"南","windSpeed":15,"day":20180805,"windScale":3},{"addTime":1533260388126,
     * "city":"宁波市","codeDay":4,"windDirectionDegree":180,"textDay":"多云","precip":0,
     * "codeNight":4,"lowTemp":27,"lastUpdate":1533168000000,"textNight":"多云","highTemp":36,
     * "windDirection":"南","windSpeed":15,"day":20180806,"windScale":3}]
     */

    private int pm25;
    private int temperature;
    private int humidity;
    private List<NextWeatherBean> nextWeather;

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
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

    public List<NextWeatherBean> getNextWeather() {
        return nextWeather;
    }

    public void setNextWeather(List<NextWeatherBean> nextWeather) {
        this.nextWeather = nextWeather;
    }

    public static class NextWeatherBean {
        /**
         * addTime : 1533260388126
         * city : 宁波市
         * codeDay : 4
         * windDirectionDegree : 180
         * textDay : 多云
         * precip : 0
         * codeNight : 4
         * lowTemp : 27
         * lastUpdate : 1533168000000
         * textNight : 多云
         * highTemp : 35
         * windDirection : 南
         * windSpeed : 15
         * day : 20180804
         * windScale : 3
         */

        private long addTime;
        private String city;
        private int codeDay;
        private int windDirectionDegree;
        private String textDay;
        private int precip;
        private int codeNight;
        private int lowTemp;
        private long lastUpdate;
        private String textNight;
        private int highTemp;
        private String windDirection;
        private int windSpeed;
        private int day;
        private int windScale;

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCodeDay() {
            return codeDay;
        }

        public void setCodeDay(int codeDay) {
            this.codeDay = codeDay;
        }

        public int getWindDirectionDegree() {
            return windDirectionDegree;
        }

        public void setWindDirectionDegree(int windDirectionDegree) {
            this.windDirectionDegree = windDirectionDegree;
        }

        public String getTextDay() {
            return textDay;
        }

        public void setTextDay(String textDay) {
            this.textDay = textDay;
        }

        public int getPrecip() {
            return precip;
        }

        public void setPrecip(int precip) {
            this.precip = precip;
        }

        public int getCodeNight() {
            return codeNight;
        }

        public void setCodeNight(int codeNight) {
            this.codeNight = codeNight;
        }

        public int getLowTemp() {
            return lowTemp;
        }

        public void setLowTemp(int lowTemp) {
            this.lowTemp = lowTemp;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }

        public String getTextNight() {
            return textNight;
        }

        public void setTextNight(String textNight) {
            this.textNight = textNight;
        }

        public int getHighTemp() {
            return highTemp;
        }

        public void setHighTemp(int highTemp) {
            this.highTemp = highTemp;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(int windSpeed) {
            this.windSpeed = windSpeed;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
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