package cc.xiaojiang.headspring.http;

public class HttpUrl {
    public static final String HOST = "http://47.98.104.64:20001/liangbo/api/";

    //登陆
    public static final String LOGIN = "account/login";
    //意见反馈
    public static final String FEEDBACK = "feedback/view";
    //获取主页空气质量接口
    public static final String WEATHER_AIR_HOUR = "weather/air/hour";
    //获取空气
    public static final String AIR = "weather/air/qry";
    /**
     * Token刷新接口
     */
    public static final String REFRESH = "token/refresh";

    /**
     * 量波动态列表
     */
    public static final String DYNAMIC_LIST = "dynamic/list";
    /**
     * 获取空气质量aqi日排名接口
     */
    public static final String AIR_RANK = "weather/aqi/rank";

    /**
     * 获取农历日期及节假日信息接口
     */
    public static final String LUNAR_INFO = "weather/lunar/info";

}
