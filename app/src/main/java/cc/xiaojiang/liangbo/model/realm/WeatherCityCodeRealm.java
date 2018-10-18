package cc.xiaojiang.liangbo.model.realm;

import io.realm.RealmObject;

public class WeatherCityCodeRealm extends RealmObject {

    /**
     * citycode : 0892
     * xz_citycode : TUZHQFX485K9
     * name : 亚东
     * adcode : 540233
     */

    private String citycode;
    private String xz_citycode;
    private String name;
    private String adcode;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getXz_citycode() {
        return xz_citycode;
    }

    public void setXz_citycode(String xz_citycode) {
        this.xz_citycode = xz_citycode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }


}
