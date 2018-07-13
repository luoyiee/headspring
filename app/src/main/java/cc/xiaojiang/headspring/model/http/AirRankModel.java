package cc.xiaojiang.headspring.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class AirRankModel {

    /**
     * province : 浙江
     * city : 宁波
     * aqi : 73
     * rank : 86
     */
    private String province;
    private String city;
    private int aqi;
    private int rank;

    public String getProvince() {
        return province == null ? "" : province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public int getAqi() {
        return aqi;
    }

    public int getRank() {
        return rank;
    }
}
