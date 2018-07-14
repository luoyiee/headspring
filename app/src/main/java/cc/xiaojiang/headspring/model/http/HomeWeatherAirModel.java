package cc.xiaojiang.headspring.model.http;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class HomeWeatherAirModel {

    /**
     * outHumidity : 49
     * pm25 : 50
     * outTemp : 31
     * nextWeather : [{"code":0,"lowTemp":25,"highTemp":34,"day":20180714},{"code":5,
     * "lowTemp":22,"highTemp":31,"day":20180715},{"code":9,"lowTemp":27,"highTemp":36,
     * "day":20180716}]
     */

    private int outHumidity;
    private int pm25;
    private int outTemp;
    private List<NextWeatherBean> nextWeather;

    public int getOutHumidity() {
        return outHumidity;
    }

    public int getPm25() {
        return pm25;
    }

    public int getOutTemp() {
        return outTemp;
    }

    public List<NextWeatherBean> getNextWeather() {
        if (nextWeather == null) {
            return new ArrayList<>();
        }
        return nextWeather;
    }

    public static class NextWeatherBean {
        /**
         * code : 0
         * lowTemp : 25
         * highTemp : 34
         * day : 20180714
         */

        private int code;
        private int lowTemp;
        private int highTemp;
        private int day;

        public int getCode() {
            return code;
        }

        public int getLowTemp() {
            return lowTemp;
        }

        public int getHighTemp() {
            return highTemp;
        }

        public int getDay() {
            return day;
        }
    }
}