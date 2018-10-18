package cc.xiaojiang.liangbo.model.http;

public class WeatherCityModel {


    /**
     * countyId : WTQ3VZQP879N
     * temperature : 24
     * text : 多云
     * countyName : 宁波
     */

    private String countyId;
    private String temperature;
    private String text;
    private String countyName;

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
