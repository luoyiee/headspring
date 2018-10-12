package cc.xiaojiang.liangbo.http;

public class HttpUrl {
//    public static final String HOST_TEST = "http://192.168.199.124:20001/liangbo/api/";
        public static final String HOST_TEST = "http://47.98.104.64:20001/liangbo/api/";
//    public static final String HOST = "http://liangbo.xjiangiot.com/liangbo/api/";
    //登陆
    public static final String LOGIN = "account/login";
    //意见反馈
    public static final String FEEDBACK = "feedback/view";
    //获取主页空气质量接口
    public static final String WEATHER_AIR_HOUR = "weather/homePage";
    //获取空气
    public static final String AIR = "weather/aqi/qry";
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

    /**
     * 查询用户信息接口
     */
    public static final String USER_INFO = "user/qry";

    /**
     * 修改用户接口
     */
    public static final String USER_MODIFY = "user/modify";

    /**
     * 获取七牛云token
     */
    public static final String QINIU_TOKEN = "token/getQiniu";
    /**
     * 获取pm2.5历史统计数据接口
     */
    public static final String PM25_HISTORY = "weather/history/pm25";

    /**
     * 量波动态点赞接口
     */
    public static final String DYNAMIC_LIKE = "dynamic/like";
    /**
     * 查询系统支持的县市
     */
    public static final String CITY_QUERY = "userCounty/qryCounty";
    /**
     * 查询用户城市列表
     */
    public static final String CITY_LIST = "userCounty/qryList";
    /**
     * 添加天气城市
     */
    public static final String CITY_ADD = "userCounty/bind";
    /**
     * 删除天气城市
     */
    public static final String CITY_DEL = "userCounty/unbind";
    /**
     * 新天气接口
     */
    public static final String NEW_WEATHER = "weather/newHomePage";

}
