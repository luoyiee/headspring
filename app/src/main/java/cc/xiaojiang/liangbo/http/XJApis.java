package cc.xiaojiang.liangbo.http;

import java.math.BigDecimal;
import java.util.List;

import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.model.LunarBean;
import cc.xiaojiang.liangbo.model.http.AirRankModel;
import cc.xiaojiang.liangbo.model.http.AqiModel;
import cc.xiaojiang.liangbo.model.http.CityAddBody;
import cc.xiaojiang.liangbo.model.http.CityIdModel;
import cc.xiaojiang.liangbo.model.http.CityQueryModel;
import cc.xiaojiang.liangbo.model.http.DynamicModel;
import cc.xiaojiang.liangbo.model.http.FeedbackBody;
import cc.xiaojiang.liangbo.model.http.HomeWeatherAirModel;
import cc.xiaojiang.liangbo.model.http.HomeNewWeatherModel;
import cc.xiaojiang.liangbo.model.http.LoginBody;
import cc.xiaojiang.liangbo.model.http.LoginModel;
import cc.xiaojiang.liangbo.model.http.LunarInfoModel;
import cc.xiaojiang.liangbo.model.http.Pm25HistoryModel;
import cc.xiaojiang.liangbo.model.http.RefreshTokenModel;
import cc.xiaojiang.liangbo.model.http.UserInfoModel;
import cc.xiaojiang.liangbo.model.http.UserModifyBody;
import cc.xiaojiang.liangbo.model.http.WeatherCityModel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface XJApis {

    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Body LoginBody loginBody);

    @POST(HttpUrl.FEEDBACK)
    Observable<BaseModel<Object>> feedback(@Body FeedbackBody feedbackBody);

    @POST(HttpUrl.REFRESH)
    Call<BaseModel<RefreshTokenModel>> refreshToken();

    @GET(HttpUrl.AIR)
    Observable<BaseModel<List<AqiModel>>> getAqi(@Query("level") int level,
                                                 @Query("longtitude") BigDecimal longtitude,
                                                 @Query("latitude") BigDecimal latitude,
                                                 @Query("longtitude2") BigDecimal longtitude2,
                                                 @Query("latitude2") BigDecimal latitude2);

    @GET(HttpUrl.WEATHER_AIR_HOUR)
    Observable<BaseModel<HomeWeatherAirModel>> queryCityWeatherAir(@Query("city") String city);

    @GET(HttpUrl.DYNAMIC_LIST)
    Observable<BaseModel<List<DynamicModel>>> dynamicList();

    @GET(HttpUrl.AIR_RANK)
    Observable<BaseModel<List<AirRankModel>>> airRankList(@Query("city") String city,
                                                          @Query("type") String type);

    @GET(HttpUrl.LUNAR_INFO)
    Observable<BaseModel<LunarInfoModel>> lunarInfo(@Query("day") String day);

    @GET(HttpUrl.USER_INFO)
    Observable<BaseModel<UserInfoModel>> userInfo();

    @POST(HttpUrl.USER_MODIFY)
    Observable<BaseModel<Object>> userModify(@Body UserModifyBody userModifyBody);

    @GET(HttpUrl.QINIU_TOKEN)
    Observable<BaseModel<String>> qiniuToken();

    @GET(HttpUrl.PM25_HISTORY)
    Observable<BaseModel<Pm25HistoryModel>> pm25History(@Query("deviceId") String deviceId,
                                                        @Query("city") String city,
                                                        @Query("type") String type,
                                                        @Query("time") int time);

    @FormUrlEncoded
    @POST(HttpUrl.DYNAMIC_LIKE)
    Observable<BaseModel<String>> dynamicLike(@Field("id") int id);

    @GET("appstore/calendar/day")
    Observable<LunarBean> lunar(@Query("key") String key, @Query("date") String date);


    //    --------------------------天气模块----------------------------------
    @GET(HttpUrl.CITY_QUERY)
    Observable<BaseModel<List<CityQueryModel>>> cityQuery(@Query("countyName") String countyName);

    @GET(HttpUrl.CITY_LIST)
    Observable<BaseModel<List<WeatherCityModel>>> cityList();

    @GET(HttpUrl.CITY_ID)
    Observable<BaseModel<CityIdModel>> cityId(@Query("citycode") String cityCode,
                                                    @Query("adcode") String adCode,
                                                    @Query("latitude") float latitude,
                                                    @Query("longtitude") float longtitude);

    @POST(HttpUrl.CITY_ADD)
    Observable<BaseModel<Object>> cityAdd(@Body CityAddBody cityAddBody);

    @POST(HttpUrl.CITY_DEL)
    Observable<BaseModel<Object>> cityDel(@Body CityAddBody cityAddBody);

    @GET(HttpUrl.NEW_WEATHER)
    Observable<BaseModel<HomeNewWeatherModel>> newWeather(@Query("countyId") String countyId);
}
