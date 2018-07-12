package cc.xiaojiang.headspring.model.http;

public class RankModel {
    private String rank;
    private String province;
    private String city;
    private String aqi;

    public String getRank() {
        return rank;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getAqi() {
        return aqi;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
}
