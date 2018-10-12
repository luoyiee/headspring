package cc.xiaojiang.liangbo.model.eventbus;

public class MessageCitySwitch {
    private String cityId;

    public MessageCitySwitch(String cityId) {
        this.cityId = cityId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
